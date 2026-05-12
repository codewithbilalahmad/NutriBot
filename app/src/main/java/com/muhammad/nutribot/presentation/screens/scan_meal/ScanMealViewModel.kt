package com.muhammad.nutribot.presentation.screens.scan_meal

import android.graphics.Bitmap
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.muhammad.nutribot.NutriBotApplication
import com.muhammad.nutribot.R
import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.domain.repository.camera.CameraController
import com.muhammad.nutribot.utils.Constants.GEMINI_API_KEY
import com.muhammad.nutribot.utils.Constants.GEMINI_MODEL_NAME
import com.muhammad.nutribot.utils.SnackbarEvent
import com.muhammad.nutribot.utils.cleanJson
import com.muhammad.nutribot.utils.decodeBitmap
import com.muhammad.nutribot.utils.detectImageContainsMeal
import com.muhammad.nutribot.utils.resizeBitmap
import com.muhammad.nutribot.utils.saveBitmapToFile
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class ScanMealViewModel(
    val cameraController: CameraController,
) : ViewModel() {
    private val context = NutriBotApplication.INSTANCE
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
    private var captureJob: Job? = null
    private val generativeModel: GenerativeModel

    init {
        val config = generationConfig {
            temperature = 0.3f
        }
        generativeModel = GenerativeModel(
            modelName = GEMINI_MODEL_NAME, apiKey = GEMINI_API_KEY, generationConfig = config
        )
    }

    fun onAction(action: ScanMealAction) {
        when (action) {
            ScanMealAction.OnCaptureMealPhoto -> onCaptureMealPhoto()
            ScanMealAction.OnNotifyNoInternetConnection -> onNotifyNoInternetConnection()
            is ScanMealAction.OnPickMealGalleryImage -> onPickMealGalleryImage(action.uri)
            is ScanMealAction.OnStartCamera -> onStartCamera(action.lifecycleOwner)
            ScanMealAction.OnToggleFlash -> onToggleFlash()
            ScanMealAction.OnToggleCameraPermissionPermanentlyDeniedDialog -> onToggleCameraPermissionPermanentlyDeniedDialog()
        }
    }

    private fun onToggleCameraPermissionPermanentlyDeniedDialog() {
        _state.update { it.copy(showCameraPermissionPermanentlyDeniedDialog = !it.showCameraPermissionPermanentlyDeniedDialog) }
    }

    private fun onStartCamera(lifecycleOwner: LifecycleOwner) {
        cameraController.startCamera(lifecycleOwner = lifecycleOwner, onCameraBinding = {
            _state.update { it.copy(isCameraLoading = true) }
        }, onCameraBindSuccess = {
            _state.update { it.copy(isCameraLoading = false) }
        }, onMealDetected = {isMealPhoto ->
            if(isMealPhoto){
                if(captureJob?.isActive == true) return@startCamera
                captureJob = viewModelScope.launch {
                    delay(1500)
                    onCaptureMealPhoto()
                }
            } else{
                captureJob?.cancel()
            }
        })
    }

    private fun onPickMealGalleryImage(uri: String) {
        val bitmap = decodeBitmap(uri) ?: return
        detectImageContainsMeal(bitmap =bitmap, onResult = { isMealPhoto ->
            if (isMealPhoto) {
                analyzeMeal(bitmap)
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

    private fun onCaptureMealPhoto() {
        cameraController.capturePhoto { bitmap ->
            analyzeMeal(bitmap)
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

    private fun analyzeMeal(bitmap: Bitmap) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isAnalyzingMeal = true) }
                val prompt = """
You are an expert AI nutrition assistant.

Analyze this food image carefully and identify all visible foods and ingredients.

STEP 1:
Determine if this image contains real food.
If not, return:
{
  "foods": [],
  "confidenceScore": 0
}

STEP 2:
Identify each visible food item separately.

Examples:
- rice
- fried egg
- grilled chicken
- fish curry
- salad
- fries

STEP 3:
For each food item, estimate realistic nutritional values:
- calories
- protein
- fat
- carbs

STEP 4:
If a food contains multiple ingredients or mixed items, extract them separately.

Examples:
- chicken rice bowl → chicken, rice, sauce
- burger → bun, beef patty, cheese
- fish curry → fish, curry sauce, rice

STEP 5:
Estimate:
- total calories
- protein
- fat
- carbs

Return STRICT JSON ONLY.

Format:

{
  "foods": [
    {
      "name": "",
      "calories": 0,
      "protein": 0,
      "fat": 0,
      "carbs": 0,
      "ingredients": [
        {
          "name": "",
          "calories": 0,
          "protein": 0,
          "fat": 0,
          "carbs": 0
        }
      ]
    }
  ],
  "confidenceScore": 0
}

IMPORTANT RULES:
- Return ONLY valid JSON
- No markdown
- No explanation text
- No null values
- Use 0 for unknown numeric values
- Use realistic nutrition estimates
- Use only visible food information
- confidenceScore must be 0–100
- calories, protein, fat, carbs must be integers
- ingredients must always exist (use empty array if needed)
""".trimIndent()
                val resizedBitmap = resizeBitmap(bitmap)
                val inputContent = content {
                    image(resizedBitmap)
                    text(prompt)
                }
                val response = generativeModel.generateContent(inputContent)
                val rawText = response.text ?: ""
                val cleaned = cleanJson(rawText)
                val food = parseFood(cleaned)
                if (food == null || food.confidenceScore < 70) {
                    _snackbarEvents.trySend(
                        SnackbarEvent.ShowSnackbar(
                            message = context.getString(R.string.low_confidence_result),
                            icon = R.drawable.ic_info
                        )
                    )
                } else {
                    val mealImageUrl = saveBitmapToFile(bitmap, "meal")
                    val finalFood =
                        food.copy(id = System.currentTimeMillis(), mealImageUrl = mealImageUrl)
                    _events.trySend(ScanMealEvent.OnMealAnalyzedSuccess(finalFood))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _snackbarEvents.trySend(
                    SnackbarEvent.ShowSnackbar(
                        message = context.getString(R.string.error_analyzing_meal),
                        icon = R.drawable.ic_info
                    )
                )
            } finally {
                _state.update { it.copy(isAnalyzingMeal = false) }
            }
        }
    }

    private fun parseFood(jsonString: String): Food? {
        return try {
            json.decodeFromString<Food>(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}