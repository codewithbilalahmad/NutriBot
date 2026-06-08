package com.muhammad.nutribot.presentation.screens.progress

import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.domain.model.NuritionChartPoint
import com.muhammad.nutribot.domain.model.NutritionCalculation
import kotlinx.datetime.LocalDate

data class ProgressState(
    val streak : Int = 0,
    val weekMeals : Map<LocalDate,List<Food>> = emptyMap(),
    val monthMeals : Map<LocalDate,List<Food>> = emptyMap(),
    val nutritionCalculation: NutritionCalculation?=null,
    val showAddFoodBottomSheet : Boolean = false
){
    val goalCalories : Int
        get() = nutritionCalculation?.calories ?: 0
    val proteinNuritionPoints : List<NuritionChartPoint>
        get() =weekMeals.map { (date, meals) ->
            NuritionChartPoint(date = date, value = meals.sumOf { it.protein })
    }
    val fatNuritionPoints : List<NuritionChartPoint>
        get() =weekMeals.map {(date, meals) ->
            NuritionChartPoint(date = date, value = meals.sumOf { it.fat })
        }
    val carbsNuritionPoints : List<NuritionChartPoint>
        get() =weekMeals.map { (date, meals) ->
            NuritionChartPoint(date = date, value = meals.sumOf { it.carbs })
        }
}