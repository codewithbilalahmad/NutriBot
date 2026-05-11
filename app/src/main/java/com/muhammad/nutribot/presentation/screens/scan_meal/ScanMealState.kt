package com.muhammad.nutribot.presentation.screens.scan_meal

data class ScanMealState(
    val isFlashOn : Boolean = false,
    val isCameraLoading : Boolean = true,
    val showCameraPermissionPermanentlyDeniedDialog : Boolean = false,
    val isAnalyzingMeal : Boolean = false
)