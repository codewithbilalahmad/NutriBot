package com.muhammad.nutribot.presentation.screens.scan_meal

import androidx.lifecycle.LifecycleOwner
import com.muhammad.nutribot.domain.model.ScanOption

sealed interface ScanMealAction{
    data class OnStartCamera(val lifecycleOwner: LifecycleOwner) : ScanMealAction
    data object OnToggleFlash : ScanMealAction
    data object OnToggleCameraPermissionPermanentlyDeniedDialog : ScanMealAction
    data object OnCaptureMealPhoto : ScanMealAction
    data object OnNotifyNoInternetConnection : ScanMealAction
    data class OnPickMealGalleryImage(val uri : String) : ScanMealAction
    data class OnScanMealOptionChange(val scanOption: ScanOption) : ScanMealAction
}