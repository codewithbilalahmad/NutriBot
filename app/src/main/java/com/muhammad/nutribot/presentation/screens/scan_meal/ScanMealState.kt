package com.muhammad.nutribot.presentation.screens.scan_meal

import android.graphics.Bitmap
import com.muhammad.nutribot.R
import com.muhammad.nutribot.domain.model.ScanOption

data class ScanMealState(
    val isFlashOn : Boolean = false,
    val isCameraLoading : Boolean = true,
    val mealDetected : Boolean = false,
    val mealBitmap : Bitmap?=null,
    val analyzingMealSteps : List<Int> = listOf(
        R.string.analyzing_your_meal,
        R.string.detecting_ingredients,
        R.string.evaluating_nutritional_values,
        R.string.checking_food_data,
        R.string.finalizing_insights
    ),
    val analyzingMealStepIndex : Int = 0,
    val showCameraPermissionPermanentlyDeniedDialog : Boolean = false,
    val isAnalyzingMeal : Boolean = false,
    val scanOption: ScanOption = ScanOption.MEAL
){
    val currentAnalyzingMealStep = analyzingMealSteps[analyzingMealStepIndex]
}