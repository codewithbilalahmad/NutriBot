package com.muhammad.nutribot.domain.repository.camera

import android.graphics.Bitmap
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner

interface CameraController {
    val previewView: PreviewView
    fun startCamera(
        lifecycleOwner: LifecycleOwner,
        onCameraBinding: () -> Unit,
        onCameraBindSuccess: () -> Unit,
        onMealDetected : (Boolean) -> Unit
    )
    fun stopCamera()
    fun capturePhoto(onPhotoCaptured: (Bitmap) -> Unit)
    fun toggleFlash()
}