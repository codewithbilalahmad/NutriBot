package com.muhammad.nutribot.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate

@Immutable
data class NutritionCaloriesPoint(
    val date: LocalDate,
    val meals: List<Food>
) {
    val proteinCalories: Int
        get() = meals.sumOf {
            it.protein * 4
        }

    val carbCalories: Int
        get() = meals.sumOf {
            it.carbs * 4
        }

    val fatCalories: Int
        get() = meals.sumOf {
            it.fat * 9
        }

    val totalCalories: Int
        get() = meals.sumOf { it.calories }
}