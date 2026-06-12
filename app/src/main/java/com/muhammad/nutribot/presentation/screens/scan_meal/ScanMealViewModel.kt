package com.muhammad.nutribot.presentation.screens.scan_meal

import android.graphics.Bitmap
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.muhammad.nutribot.NutriBotApplication
import com.muhammad.nutribot.R
import com.muhammad.nutribot.domain.model.ScanOption
import com.muhammad.nutribot.domain.model.ScannedMeal
import com.muhammad.nutribot.domain.model.toFood
import com.muhammad.nutribot.domain.repository.barcode_meal.BarcodeMealRepository
import com.muhammad.nutribot.domain.repository.camera.CameraController
import com.muhammad.nutribot.utils.Constants.GEMINI_API_KEY
import com.muhammad.nutribot.utils.Constants.GEMINI_MODEL_NAME
import com.muhammad.nutribot.utils.Result
import com.muhammad.nutribot.utils.SnackbarEvent
import com.muhammad.nutribot.utils.cleanJson
import com.muhammad.nutribot.utils.decodeBitmap
import com.muhammad.nutribot.utils.detectImageContainsMeal
import com.muhammad.nutribot.utils.resizeBitmap
import com.muhammad.nutribot.utils.saveBitmapToFile
import com.muhammad.nutribot.utils.scanBarcodeFromBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class ScanMealViewModel(
    savedStateHandle: SavedStateHandle,
    val cameraController: CameraController,
    private val barcodeMealRepository: BarcodeMealRepository,
) : ViewModel() {
    private val context = NutriBotApplication.INSTANCE
    private val galleryUri = savedStateHandle.get<String?>("galleryUri")
    private var analyzingStepJob: Job? = null
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }
    private val _state = MutableStateFlow(ScanMealState())
    val state = _state.asStateFlow()
    private val _events = Channel<ScanMealEvent>()
    val events = _events.receiveAsFlow()
    private val _snackbarEvents = Channel<SnackbarEvent>()
    val snackbarEvents = _snackbarEvents.receiveAsFlow()
    private val generativeModel: GenerativeModel
    private var barcodeInProgress = false

    init {
        _state.update { it.copy(galleryUri = galleryUri) }
        val config = generationConfig {
            temperature = 0.3f
        }
        generativeModel = GenerativeModel(
            modelName = GEMINI_MODEL_NAME, apiKey = GEMINI_API_KEY, generationConfig = config
        )
        if (galleryUri != null) {
            onPickMealGalleryImage(galleryUri)
        }
    }

    fun onAction(action: ScanMealAction) {
        when (action) {
            is ScanMealAction.OnCaptureMealPhoto -> onCaptureMealPhoto(action.lifecycleOwner)
            ScanMealAction.OnNotifyNoInternetConnection -> onNotifyNoInternetConnection()
            is ScanMealAction.OnPickMealGalleryImage -> onPickMealGalleryImage(
                uri = action.uri,
                lifecycleOwner = action.lifecycleOwner
            )

            is ScanMealAction.OnStartCamera -> onStartCamera(action.lifecycleOwner)
            ScanMealAction.OnToggleFlash -> onToggleFlash()
            ScanMealAction.OnToggleCameraPermissionPermanentlyDeniedDialog -> onToggleCameraPermissionPermanentlyDeniedDialog()
            is ScanMealAction.OnScanMealOptionChange -> onScanMealOptionChange(
                scanOption = action.scanOption,
                lifecycleOwner = action.lifecycleOwner
            )

            ScanMealAction.OnToggleBarcodeNumberSection -> onToggleBarcodeNumberSection()
            is ScanMealAction.OnAnalyzeBarcodeMeal -> analyzeBarcodeMeal(
                barcode = action.barcode,
                bitmap = action.bitmap,
                lifecycleOwner = action.lifecycleOwner
            )

            is ScanMealAction.OnPickBarcodeGalleryImage -> onPickBarcodeGalleryImage(
                uri = action.uri,
                lifecycleOwner = action.lifecycleOwner
            )
        }
    }

    private fun onPickBarcodeGalleryImage(
        uri: String,
        lifecycleOwner: LifecycleOwner
    ) {
        val bitmap = decodeBitmap(uri) ?: return
        scanBarcodeFromBitmap(bitmap = bitmap, onSuccess = {barcode ->
            analyzeBarcodeMeal(barcode = barcode, bitmap = bitmap, lifecycleOwner = lifecycleOwner)
        }, onFailure = {
            _snackbarEvents.trySend(
                SnackbarEvent.ShowSnackbar(
                    message = context.getString(R.string.no_barcode_detected),
                    icon = R.drawable.ic_barcode
                )
            )
        })
    }

    private fun onToggleBarcodeNumberSection() {
        _state.update { it.copy(showBarcodeNumberSection = !it.showBarcodeNumberSection) }
    }

    private fun onScanMealOptionChange(scanOption: ScanOption, lifecycleOwner: LifecycleOwner) {
        if (state.value.scanOption == scanOption) return
        _state.update { it.copy(scanOption = scanOption) }
        cameraController.stopCamera()
        cameraController.startCamera(
            lifecycleOwner = lifecycleOwner,
            onCameraBinding = {
                _state.update { it.copy(isCameraLoading = true) }
            },
            onCameraBindSuccess = {
                _state.update { it.copy(isCameraLoading = false) }
            },
            onMealDetected = { mealDetected ->

                _state.update {
                    it.copy(mealDetected = mealDetected)
                }
            }, onBarcodeDetected = { barcode, bitmap ->
                analyzeBarcodeMeal(
                    barcode = barcode,
                    bitmap = bitmap,
                    lifecycleOwner = lifecycleOwner
                )
            },
            scanOption = scanOption
        )
    }

    private fun onToggleCameraPermissionPermanentlyDeniedDialog() {
        _state.update { it.copy(showCameraPermissionPermanentlyDeniedDialog = !it.showCameraPermissionPermanentlyDeniedDialog) }
    }

    private fun onStartCamera(lifecycleOwner: LifecycleOwner) {
        cameraController.resetBarcodeAnalyzer()
        cameraController.startCamera(
            lifecycleOwner = lifecycleOwner,
            onCameraBinding = {
                _state.update { it.copy(isCameraLoading = true) }
            },
            onCameraBindSuccess = {
                _state.update { it.copy(isCameraLoading = false) }
            },
            onMealDetected = { mealDetected ->

                _state.update {
                    it.copy(mealDetected = mealDetected)
                }
            }, scanOption = state.value.scanOption, onBarcodeDetected = { barcode, bitmap ->
                analyzeBarcodeMeal(
                    barcode = barcode,
                    bitmap = bitmap,
                    lifecycleOwner = lifecycleOwner
                )
            }
        )
    }

    private fun onPickMealGalleryImage(uri: String, lifecycleOwner: LifecycleOwner) {
        val bitmap = decodeBitmap(uri) ?: return
        detectImageContainsMeal(bitmap = bitmap, onResult = { isMealPhoto ->
            if (isMealPhoto) {
                analyzeMeal(bitmap = bitmap, lifecycleOwner = lifecycleOwner)
            } else {
                _snackbarEvents.trySend(
                    SnackbarEvent.ShowSnackbar(
                        message = context.getString(R.string.no_meal_detected),
                        icon = R.drawable.ic_launcher_foreground
                    )
                )
            }
        })
    }
    private fun onPickMealGalleryImage(uri: String) {
        val bitmap = decodeBitmap(uri) ?: return
        detectImageContainsMeal(bitmap = bitmap, onResult = { isMealPhoto ->
            if (isMealPhoto) {
                analyzeMeal(bitmap = bitmap)
            } else {
                _snackbarEvents.trySend(
                    SnackbarEvent.ShowSnackbar(
                        message = context.getString(R.string.no_meal_detected),
                        icon = R.drawable.ic_launcher_foreground
                    )
                )
            }
        })
    }

    private fun onCaptureMealPhoto(lifecycleOwner: LifecycleOwner) {
        cameraController.capturePhoto { bitmap ->
            analyzeMeal(bitmap = bitmap, lifecycleOwner = lifecycleOwner)
        }
    }

    private fun onNotifyNoInternetConnection() {
        _snackbarEvents.trySend(
            SnackbarEvent.ShowSnackbar(
                message = context.getString(R.string.no_internet_connection),
                icon = R.drawable.ic_no_wifi
            )
        )
    }

    private fun onToggleFlash() {
        _state.update { it.copy(isFlashOn = !it.isFlashOn) }
        cameraController.toggleFlash()
    }

    private fun analyzeMeal(bitmap: Bitmap, lifecycleOwner: LifecycleOwner?=null) {
        viewModelScope.launch {
            try {
                if(lifecycleOwner != null){
                    cameraController.stopCamera()
                }
                _state.update {
                    it.copy(
                        mealBitmap = bitmap,
                        isAnalyzingMeal = true,
                        analyzingMealStepIndex = 0
                    )
                }
                analyzingStepJob?.cancel()
                analyzingStepJob = viewModelScope.launch {
                    val steps = state.value.analyzingMealSteps

                    for (i in steps.indices) {
                        if (!isActive) break
                        _state.update { it.copy(analyzingMealStepIndex = i) }
                        delay(2000)
                    }
                }
                val resizedBitmap = withContext(Dispatchers.Default) {
                    resizeBitmap(bitmap)
                }
                val prompt = """
Analyze this food image for a nutrition tracking app.

Return ONLY valid JSON.

FORMAT:

{
  "name": "",
  "calories": 0,
  "protein": 0,
  "fat": 0,
  "carbs": 0,
  "confidenceScore": 85,
  "ingredients": [
    {
      "name": "",
      "calories": 0,
      "protein": 0,
      "fat": 0,
      "carbs": 0,
      "isSelected": true
    }
  ]
}

RULES:

GENERAL:
- confidenceScore MUST be INTEGER between 0 and 100
- No decimals allowed anywhere for confidenceScore
- Only analyze visible ready-to-eat food items
- Ignore cooking components completely

NAME RULE (IMPORTANT):
- "name" MUST include the MAIN DISH + visible components separated by commas
- Example:
  - "Chicken Burger, Fries"
  - "Chicken Biryani, Salad"
  - "Rice, Chicken Curry"
  - "Apple"
- Do NOT include cooking ingredients (oil, salt, spices, etc.)

INGREDIENT RULES (VERY IMPORTANT):
- ONLY include "ingredients" when there are MULTIPLE DISTINCT FOOD DISHES
- Ingredients are ONLY for separate meal items, NOT cooking components

VALID EXAMPLES:
✔ Burger + Fries → ingredients = [Burger, Fries]
✔ Rice + Chicken Curry → ingredients = [Rice, Chicken Curry]

INVALID (NEVER DO THIS):
❌ oil, salt, garlic, spices, butter

SINGLE FOOD RULE:
- If only one dish exists:
  → ingredients MUST be []

OUTPUT RULES:
- No extra fields
- No markdown
- No explanation
- Always return valid JSON
""".trimIndent()
                val inputContent = content {
                    image(resizedBitmap)
                    text(prompt)
                }
                val response = withContext(Dispatchers.IO) {
                    generativeModel.generateContent(inputContent)
                }
                val rawText = response.text ?: ""
                val cleaned = cleanJson(rawText)
                val meal = parseScannedMeal(cleaned)
                if (meal == null) {
                    _snackbarEvents.trySend(
                        SnackbarEvent.ShowSnackbar(
                            message = context.getString(R.string.low_confidence_result),
                            icon = R.drawable.ic_info
                        )
                    )
                } else {
                    val mealImageUrl = saveBitmapToFile(bitmap, "meal")
                    val food = meal.toFood(mealImageUrl = mealImageUrl)
                    _events.trySend(ScanMealEvent.OnMealAnalyzedSuccess(food))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                if(lifecycleOwner != null) {
                    cameraController.startCamera(
                        lifecycleOwner = lifecycleOwner,
                        onCameraBinding = {
                            _state.update { it.copy(isCameraLoading = true) }
                        },
                        onCameraBindSuccess = {
                            _state.update { it.copy(isCameraLoading = false) }
                        },
                        onMealDetected = { mealDetected ->

                            _state.update {
                                it.copy(mealDetected = mealDetected)
                            }
                        },
                        scanOption = state.value.scanOption,
                        onBarcodeDetected = { barcode, bitmap ->
                            analyzeBarcodeMeal(
                                barcode = barcode,
                                bitmap = bitmap,
                                lifecycleOwner = lifecycleOwner
                            )
                        })
                }
                _snackbarEvents.trySend(
                    SnackbarEvent.ShowSnackbar(
                        message = context.getString(R.string.error_analyzing_meal),
                        icon = R.drawable.ic_info
                    )
                )
            } finally {
                _state.update {
                    it.copy(
                        isAnalyzingMeal = false,
                        mealBitmap = null,
                        analyzingMealStepIndex = 0,
                        galleryUri = null
                    )
                }
            }
        }
    }

    private fun analyzeBarcodeMeal(
        barcode: String,
        bitmap: Bitmap?,
        lifecycleOwner: LifecycleOwner,
    ) {
        if (barcodeInProgress) return
        barcodeInProgress = true
        viewModelScope.launch {
            try {
                cameraController.stopCamera()
                _state.update { it.copy(mealBitmap = bitmap, isAnalyzingMeal = true) }
                val foodResponse = withContext(Dispatchers.IO) {
                    barcodeMealRepository.getBarcodeMeal(barcode)
                }
                _state.update { it.copy(isAnalyzingMeal = false) }
                barcodeInProgress = false
                when (foodResponse) {
                    is Result.Error -> {
                        _snackbarEvents.trySend(
                            SnackbarEvent.ShowSnackbar(
                                message = context.getString(R.string.error_analyzing_meal),
                                icon = R.drawable.ic_info
                            )
                        )
                    }

                    is Result.Success -> {
                        foodResponse.data?.let { food ->
                            _events.trySend(ScanMealEvent.OnMealAnalyzedSuccess(food))
                        }
                    }
                }
            } finally {
                _state.update { it.copy(isAnalyzingMeal = false, mealBitmap = null) }
                barcodeInProgress = false
                cameraController.startCamera(
                    lifecycleOwner = lifecycleOwner,
                    onCameraBinding = {
                        _state.update { it.copy(isCameraLoading = true) }
                    },
                    onCameraBindSuccess = {
                        _state.update { it.copy(isCameraLoading = false) }
                    },
                    onMealDetected = { mealDetected ->

                        _state.update {
                            it.copy(mealDetected = mealDetected)
                        }
                    }, onBarcodeDetected = { barcode, bitmap ->
                        analyzeBarcodeMeal(
                            barcode = barcode,
                            bitmap = bitmap,
                            lifecycleOwner = lifecycleOwner
                        )
                    },
                    scanOption = state.value.scanOption
                )
            }
        }
    }

    private fun parseScannedMeal(jsonString: String): ScannedMeal? {
        return try {
            val cleanedJson =
                jsonString.replace(Regex("\"confidenceScore\"\\s*:\\s*(\\d+)\\.(\\d+)")) {
                    val intValue = it.groupValues[1]
                    "\"confidenceScore\": $intValue"
                }
            json.decodeFromString<ScannedMeal>(cleanedJson)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}