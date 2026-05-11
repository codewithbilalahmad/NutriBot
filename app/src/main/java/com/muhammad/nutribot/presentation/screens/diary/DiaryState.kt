package com.muhammad.nutribot.presentation.screens.diary

import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.domain.model.NutritionCalculation
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

data class DiaryState(
    val foods: List<Food> = emptyList(),
    val streak : Int= 0,
    val nutritionCalculation: NutritionCalculation?=null,
    val showAddFoodBottomSheet : Boolean = false,
    val selectedDate : LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
){
    val weekRange = -100..0
    val eatenCalories : Int = foods.sumOf { it.calories }
    val goalCalories = nutritionCalculation?.calories ?: 0
    val goalProteinGrams = nutritionCalculation?.proteinGrams ?: 0
    val goalCarbsGrams = nutritionCalculation?.carbsGrams ?: 0
    val goalFatGrams = nutritionCalculation?.fatGrams ?: 0
    val eatenProteinGrams = foods.sumOf { it.protein }
    val eatenCarbsGrams = foods.sumOf { it.carbs }
    val eatenFatGrams = foods.sumOf { it.fat }
}
