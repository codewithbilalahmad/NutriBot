package com.muhammad.nutribot.data.repository.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.YuvImage
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.*
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.io.ByteArrayOutputStream

class MealBarcodeAnalyzer(
    private val onBarcodeDetected: (String, Bitmap) -> Unit,
) : ImageAnalysis.Analyzer{
    private val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder().setBarcodeFormats(
            Barcode.FORMAT_EAN_13,
            Barcode.FORMAT_EAN_8,
            Barcode.FORMAT_UPC_A,
            Barcode.FORMAT_UPC_E
        ).build()
    )
    private var isProcessing = false
    private var lastAnalyzedTime = 0L
    private var barcodeLocked = false
    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy : ImageProxy) {
        if(barcodeLocked){
            imageProxy.close()
            return
        }
        val currentTime = System.currentTimeMillis()
        if(currentTime - lastAnalyzedTime < 2000){
            imageProxy.close()
            return
        }
        if(isProcessing){
            imageProxy.close()
            return
        }
        val mediaImage = imageProxy.image ?: run {
            imageProxy.close()
            return
        }
        isProcessing = true
        val bitmap = imageProxyToBitmap(imageProxy)
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        scanner.process(image).addOnSuccessListener { barcodes ->
            val barcode = barcodes.firstOrNull()?.rawValue
            if(!barcode.isNullOrEmpty()){
                barcodeLocked = true
                lastAnalyzedTime = currentTime
                onBarcodeDetected(barcode,bitmap)
            }
        }.addOnFailureListener {
            it.printStackTrace()
        }.addOnCompleteListener {
            isProcessing = false
            imageProxy.close()
        }
    }
    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {

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
            android.graphics.Rect(0, 0, image.width, image.height),
            80,
            out
        )

        val bytes = out.toByteArray()

        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

        return rotateBitmap(bitmap, image.imageInfo.rotationDegrees)
    }
    private fun rotateBitmap(source: Bitmap, rotation: Int): Bitmap {
        if (rotation == 0) return source

        val matrix = Matrix().apply {
            postRotate(rotation.toFloat())
        }

        return Bitmap.createBitmap(
            source,
            0,
            0,
            source.width,
            source.height,
            matrix,
            true
        )
    }
    fun reset(){
        barcodeLocked = false
    }
}