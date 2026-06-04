package com.muhammad.nutribot.data.repository.camera

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.*
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class MealBarcodeAnalyzer(
    private val onBarcodeDetected: (String) -> Unit,
) : ImageAnalysis.Analyzer{
    private val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder().setBarcodeFormats(
            Barcode.FORMAT_EAN_13,
            Barcode.FORMAT_EAN_8,
            Barcode.FORMAT_UPC_A,
            Barcode.FORMAT_UPC_E
        ).build()
    )

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy : ImageProxy) {
        val mediaImage = imageProxy.image
        if(mediaImage == null){
            imageProxy.close()
            return
        }
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        scanner.process(image).addOnSuccessListener { barcodes ->
            val barcode = barcodes.firstOrNull()?.rawValue
            if(!barcode.isNullOrEmpty()){
                onBarcodeDetected(barcode)
            }
        }.addOnCompleteListener {
            imageProxy.close()
        }
    }
}