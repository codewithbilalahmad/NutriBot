package com.muhammad.nutribot.presentation.screens.scan_meal

import android.graphics.Bitmap
import androidx.lifecycle.LifecycleOwner
import com.muhammad.nutribot.domain.model.ScanOption

sealed interface ScanMealAction{
    data class OnStartCamera(val lifecycleOwner: LifecycleOwner) : ScanMealAction
    data object OnToggleFlash : ScanMealAction
    data object OnToggleCameraPermissionPermanentlyDeniedDialog : ScanMealAction
    data class OnCaptureMealPhoto(val lifecycleOwner: LifecycleOwner) : ScanMealAction
    data object OnNotifyNoInternetConnection : ScanMealAction
    data class OnPickMealGalleryImage(val uri : String, val lifecycleOwner: LifecycleOwner) : ScanMealAction
    data class OnPickBarcodeGalleryImage(val uri : String, val lifecycleOwner: LifecycleOwner) : ScanMealAction
    data class OnScanMealOptionChange(val lifecycleOwner: LifecycleOwner,val scanOption: ScanOption) : ScanMealAction
    data object OnToggleBarcodeNumberSection : ScanMealAction
    data class OnAnalyzeBarcodeMeal(val barcode: String,val bitmap : Bitmap?,val lifecycleOwner: LifecycleOwner) : ScanMealAction
}