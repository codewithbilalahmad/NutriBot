package com.muhammad.nutribot.data.repository.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.YuvImage
import android.media.MediaActionSound
import android.view.OrientationEventListener
import android.view.Surface
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.UseCaseGroup
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.muhammad.nutribot.domain.repository.camera.CameraController
import java.io.ByteArrayOutputStream

class CameraControllerImp(
    private val context: Context,
) : CameraController {
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var lifeCycleOwner: LifecycleOwner? = null
    private var mediaActionSound: MediaActionSound? = null
    private lateinit var imageCapture: ImageCapture
    private lateinit var preview: Preview
    private var isFlashOn = false
    private var orientationEventListener: OrientationEventListener? = null
    private var currentSurfaceRotation = Surface.ROTATION_0

    override val previewView = PreviewView(context).apply {
        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        scaleType = PreviewView.ScaleType.FILL_CENTER
    }

    private fun setupOrientationListener() {
        orientationEventListener = object : OrientationEventListener(context) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) return

                val rotation = when (orientation) {
                    in 45..134 -> Surface.ROTATION_270
                    in 135..224 -> Surface.ROTATION_180
                    in 225..314 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }

                if (rotation != currentSurfaceRotation) {
                    currentSurfaceRotation = rotation
                    if (::imageCapture.isInitialized) {
                        imageCapture.targetRotation = rotation
                    }
                }
            }
        }.also { it.enable() }
    }

    override fun startCamera(
        lifecycleOwner: LifecycleOwner,
        onCameraBinding: () -> Unit,
        onCameraBindSuccess: () -> Unit,
        onMealDetected: (Boolean) -> Unit,
    ) {
        lifeCycleOwner = lifecycleOwner
        onCameraBinding()
        setupOrientationListener()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindUseCases(onMealDetected)
            previewView.previewStreamState.observe(lifecycleOwner) { state ->
                if (state == PreviewView.StreamState.STREAMING) {
                    onCameraBindSuccess()
                }
            }
        }, ContextCompat.getMainExecutor(context))
    }

    override fun stopCamera() {
        orientationEventListener?.disable()
        orientationEventListener = null
        cameraProvider?.unbindAll()
        lifeCycleOwner = null
        cameraProvider = null
        camera = null
    }

    override fun capturePhoto(onPhotoCaptured: (Bitmap) -> Unit) {
        mediaActionSound?.play(MediaActionSound.SHUTTER_CLICK)

        imageCapture.takePicture(
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageCapturedCallback() {

                override fun onCaptureSuccess(image: ImageProxy) {
                    try {
                        val bitmap = imageProxyToBitmap(image)
                        val rotatedBitmap = rotateBitmap(bitmap, image.imageInfo.rotationDegrees)
                        onPhotoCaptured(rotatedBitmap)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        image.close()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                }
            }
        )
    }

    override fun toggleFlash() {
        isFlashOn = !isFlashOn
        camera?.cameraControl?.enableTorch(isFlashOn)
    }

    private fun bindUseCases(onMealDetected: (Boolean) -> Unit) {
        val provider = cameraProvider ?: return
        val lifecycleOwner = lifeCycleOwner ?: return
        provider.unbindAll()
        val selector = CameraSelector.Builder().build()
        preview = Preview.Builder().build()
        preview.surfaceProvider = previewView.surfaceProvider
        imageCapture = ImageCapture.Builder()
            .setFlashMode(ImageCapture.FLASH_MODE_OFF)
            .setTargetRotation(currentSurfaceRotation)
            .build()
        val imageAnalysis = ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build().apply {
                setAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    MealDetectionAnalyzer(onMealDetected)
                )
            }
        val useCaseGroupBuilder = UseCaseGroup.Builder()
            .addUseCase(preview)
            .addUseCase(imageCapture)
            .addUseCase(imageAnalysis)

        camera = provider.bindToLifecycle(lifecycleOwner, selector, useCaseGroupBuilder.build())
        mediaActionSound = MediaActionSound()
    }

    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        return when (image.format) {
            ImageFormat.JPEG -> {
                val buffer = image.planes[0].buffer
                val bytes = ByteArray(buffer.remaining())
                buffer.get(bytes)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            }

            ImageFormat.YUV_420_888 -> {
                imageProxyYuvToBitmap(image)
            }

            else -> {
                val buffer = image.planes[0].buffer
                val bytes = ByteArray(buffer.remaining())
                buffer.get(bytes)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            }
        }
    }

    private fun imageProxyYuvToBitmap(image: ImageProxy): Bitmap {
        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(
            nv21,
            ImageFormat.NV21,
            image.width,
            image.height,
            null
        )

        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(
            Rect(0, 0, image.width, image.height),
            100,
            out
        )

        val imageBytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    private fun rotateBitmap(bitmap: Bitmap, rotation: Int): Bitmap {
        val matrix = Matrix().apply {
            postRotate(rotation.toFloat())
        }
        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
    }
}