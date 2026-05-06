package com.muhammad.nutribot.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class NutritionCalculation(
    val calories : Int,
    val proteinPercent : Int,
    val carbsPercent : Int,
    val fatPercent : Int,
    val proteinGrams : Int,
    val carbsGrams : Int,
    val fatGrams : Int,
)