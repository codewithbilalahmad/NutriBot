package com.muhammad.nutribot.domain.repository.camera

import android.graphics.Bitmap
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.muhammad.nutribot.domain.model.ScanOption

interface CameraController {
    val previewView: PreviewView
    fun startCamera(
        lifecycleOwner: LifecycleOwner,
        scanOption: ScanOption,
        onBarcodeDetected: (String, Bitmap) -> Unit,
        onCameraBinding: () -> Unit,
        onCameraBindSuccess: () -> Unit,
        onMealDetected: (Boolean) -> Unit,
    )
    fun stopCamera()
    fun capturePhoto(onPhotoCaptured: (Bitmap) -> Unit)
    fun toggleFlash()
    fun resetBarcodeAnalyzer()
}