package com.muhammad.nutribot.presentation.screens.scan_meal

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.muhammad.nutribot.presentation.components.snakbar.GradientSnackbarHost
import com.muhammad.nutribot.domain.model.GradientSnackbarVisuals
import com.muhammad.nutribot.presentation.screens.scan_meal.components.CameraPermissionCard
import com.muhammad.nutribot.utils.ObserveAsEvents
import com.muhammad.nutribot.utils.SnackbarEvent
import com.muhammad.nutribot.utils.checkPermissionGranted
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import com.muhammad.nutribot.R
import com.muhammad.nutribot.presentation.screens.scan_meal.components.CameraPreview
import com.muhammad.nutribot.presentation.screens.scan_meal.components.MealCorneredBox
import com.muhammad.nutribot.presentation.screens.scan_meal.components.ScanMealBottomBar
import com.muhammad.nutribot.presentation.theme.NutriBotTheme

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ScanMealScreen(
    navHostController: NavHostController,
    isInternetConnected: Boolean,
    viewModel: ScanMealViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val controller = viewModel.cameraController
    val scope = rememberCoroutineScope()
    val infiniteTransition = rememberInfiniteTransition()
    val snackbarHostState = remember { SnackbarHostState() }
    val layoutDirection = LocalLayoutDirection.current
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    val activity = context as Activity
    var cameraPermissionGranted by remember {
        mutableStateOf(checkPermissionGranted(context, Manifest.permission.CAMERA))
    }
    val photoPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.onAction(ScanMealAction.OnPickMealGalleryImage(uri.toString()))
            }
        }
    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            cameraPermissionGranted = isGranted
            val cameraPermanentlyDenied =
                !isGranted && !ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.CAMERA
                )
            if (cameraPermanentlyDenied) {
                viewModel.onAction(ScanMealAction.OnToggleCameraPermissionPermanentlyDeniedDialog)
            }
        }
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ScanMealEvent.OnMealAnalyzedSuccess -> {
                println("Meal Data : ${event.food}")
            }
        }
    }
    ObserveAsEvents(viewModel.snackbarEvents) { event ->
        when (event) {
            is SnackbarEvent.ShowSnackbar -> {
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(
                        GradientSnackbarVisuals(
                            icon = event.icon,
                            message = event.message,
                            duration = event.duration,
                            actionLabel = event.actionLabel,
                            withDismissAction = false
                        )
                    )
                }
            }
        }
    }
    DisposableEffect(lifeCycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                cameraPermissionGranted = checkPermissionGranted(context, Manifest.permission.CAMERA)
                if (cameraPermissionGranted && !state.isAnalyzingMeal) {
                    viewModel.onAction(ScanMealAction.OnStartCamera(lifeCycleOwner))
                }
            }
        }

        lifeCycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }
    NutriBotTheme(darkTheme = true) {
        Scaffold(
            modifier = Modifier.fillMaxSize(), snackbarHost = {
                GradientSnackbarHost(snackbarHostState = snackbarHostState)
            }, topBar = {
                if (!state.isAnalyzingMeal) {
                    TopAppBar(
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    navHostController.navigateUp()
                                },
                                shapes = IconButtonDefaults.shapes(),
                                modifier = Modifier.padding(start = 8.dp),
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = if (cameraPermissionGranted) MaterialTheme.colorScheme.background.copy(
                                        0.2f
                                    ) else MaterialTheme.colorScheme.surfaceContainer
                                )
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left),
                                    contentDescription = null
                                )
                            }
                        },
                        title = {},
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                    )
                }
            }, bottomBar = {
                if (!state.isAnalyzingMeal) {
                    ScanMealBottomBar(
                        isFlashOn = state.isFlashOn,
                        isCaptureButtonEnabled = cameraPermissionGranted && state.mealDetected,
                        enabled = cameraPermissionGranted,
                        onCaptureMealPhoto = {
                            if (isInternetConnected) {
                                viewModel.onAction(ScanMealAction.OnCaptureMealPhoto)
                            } else {
                                viewModel.onAction(ScanMealAction.OnNotifyNoInternetConnection)
                            }
                        },
                        onPickMealGalleryImage = {
                            if (isInternetConnected) {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            } else {
                                viewModel.onAction(ScanMealAction.OnNotifyNoInternetConnection)
                            }
                        },
                        onToggleFlash = {
                            viewModel.onAction(ScanMealAction.OnToggleFlash)
                        },
                        selectedScanOption = state.scanOption,
                        onSelectScanOption = { scanOption ->
                            viewModel.onAction(ScanMealAction.OnScanMealOptionChange(scanOption))
                        })
                }
            }
        ) { paddingValues ->
            if (cameraPermissionGranted) {
                if (state.isAnalyzingMeal) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        state.mealBitmap?.let { bitmap ->
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            MealCorneredBox(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(configuration.screenHeightDp.dp * 0.4f)
                                    .padding(horizontal = 24.dp)
                                    .align(Alignment.Center)
                            )
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(horizontal = 24.dp, vertical = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                LoadingIndicator(color = MaterialTheme.colorScheme.onBackground)
                                Text(
                                    text = stringResource(state.currentAnalyzingMealStep),
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        color = MaterialTheme.colorScheme.onBackground,
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                start = paddingValues.calculateLeftPadding(layoutDirection),
                                end = paddingValues.calculateEndPadding(layoutDirection)
                            )
                    ) {
                        CameraPreview(
                            modifier = Modifier.fillMaxSize(), previewView = controller.previewView
                        )
                        if (state.isCameraLoading) {
                            LoadingIndicator(
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        } else {
                            MealCorneredBox(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(configuration.screenHeightDp.dp * 0.4f)
                                    .padding(horizontal = 24.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.camera_photo),
                        contentDescription = null,
                        modifier = Modifier.size(180.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.allow_app_to_access_your_camera),
                        modifier = Modifier
                            .fillMaxWidth(0.80f),
                        style = MaterialTheme.typography.titleMedium.copy(
                            textAlign = TextAlign.Center, fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(Modifier.height(20.dp))
                    Text(
                        text = stringResource(R.string.allow_app_to_access_your_camera_desp),
                        modifier = Modifier.fillMaxWidth(0.85f),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.surface,
                            textAlign = TextAlign.Center
                        )
                    )
                    Spacer(Modifier.height(30.dp))
                    CameraPermissionCard(
                        cameraPermissionGranted = cameraPermissionGranted,
                        modifier = Modifier.fillMaxWidth(0.9f),
                        onRequestCameraPermission = {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        })
                }
            }
        }
    }
}