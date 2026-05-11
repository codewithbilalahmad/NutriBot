package com.muhammad.nutribot.data.repository.camera

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.muhammad.nutribot.utils.MEAL_LABELS

class MealDetectionAnalyzer(
    private val onMealDetected: (Boolean) -> Unit,
) : ImageAnalysis.Analyzer {
    private val labeler by lazy {
        val options = ImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.6f)
            .build()
        ImageLabeling.getClient(options)
    }

    private var lastResult = false

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }
        val rotation = imageProxy.imageInfo.rotationDegrees
        val image = InputImage.fromMediaImage(
            mediaImage,
            rotation
        )
        labeler.process(image)
            .addOnSuccessListener { labels ->

                val isCar = labels.any { label ->
                    val text = label.text.lowercase()
                    MEAL_LABELS.any { keyword ->
                        text.contains(keyword)
                    }
                }

                if (isCar != lastResult) {
                    lastResult = isCar
                    onCarDetected(isCar)
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
}