package com.muhammad.nutribot.domain.model

import androidx.compose.runtime.Immutable
import com.muhammad.nutribot.utils.generateId
import kotlinx.serialization.Serializable
import kotlin.time.Clock

@Immutable
@Serializable
data class ScannedMeal(
    val name: String = "",
    val calories: Int = 0,
    val protein: Int = 0,
    val fat: Int = 0,
    val carbs: Int = 0,
    val confidenceScore: Int = 0,
    val ingredients: List<ScannedIngredient> = emptyList()
)

fun ScannedMeal.toFood(mealImageUrl: String): Food {
    val foodId = generateId()

    return Food(
        id = foodId,
        name = name,
        mealImageUrl = mealImageUrl,
        calories = calories,
        protein = protein,
        fat = fat,
        carbs = carbs,
        numberOfServings = 1,
        eatenAt = Clock.System.now(),
        confidenceScore = confidenceScore,
        ingredients = ingredients.map {
            Ingredient(
                id = generateId(),
                foodId = foodId,
                name = it.name,
                calories = it.calories,
                protein = it.protein,
                fat = it.fat,
                carbs = it.carbs,
                isSelected = it.isSelected
            )
        }
    )
}