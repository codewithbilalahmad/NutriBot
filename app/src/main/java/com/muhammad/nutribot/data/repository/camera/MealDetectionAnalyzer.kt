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
    private var isProcessing = false

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {

        if (isProcessing) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image

        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        isProcessing = true

        val image = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )

        labeler.process(image)
            .addOnSuccessListener { labels ->

                val isMeal = labels.any { label ->
                    val text = label.text.lowercase()

                    MEAL_LABELS.any { keyword ->
                        text.contains(keyword)
                    }
                }

                if (isMeal != lastResult) {
                    lastResult = isMeal
                    onMealDetected(isMeal)
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
            .addOnCompleteListener {
                isProcessing = false
                imageProxy.close()
            }
    }
}