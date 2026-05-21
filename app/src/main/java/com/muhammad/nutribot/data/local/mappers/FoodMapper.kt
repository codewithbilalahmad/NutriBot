package com.muhammad.nutribot.data.local.mappers

import com.muhammad.nutribot.data.local.entity.FoodEntity
import com.muhammad.nutribot.data.local.relations.FoodWithWithIngredients
import com.muhammad.nutribot.domain.model.Food
import kotlin.time.Instant

fun FoodWithWithIngredients.toFood(): Food {
    return Food(
        id = food.id,
        name = food.name,
        calories = food.calories,
        fat = food.fat,
        protein = food.protein,
        carbs = food.carbs,
        eatenAt = food.eatenAt,
        ingredients = ingredients.map { it.toIngredient() },
        numberOfServings = food.numberOfServings,
        confidenceScore = food.confidenceScore,
    )
}

fun FoodEntity.toFood(): Food {
    return Food(
        id = id,
        name = name,
        calories = calories,
        fat = fat,
        protein = protein,
        carbs = carbs,
        eatenAt = eatenAt,
        ingredients = emptyList(),
        confidenceScore = confidenceScore,
        numberOfServings = numberOfServings
    )
}

fun Food.toFoodEntity(): FoodEntity {
    return FoodEntity(
        id = id,
        name = name,
        calories = calories,
        fat = fat,
        protein = protein,
        carbs = carbs,
        eatenAt = eatenAt,
        numberOfServings = numberOfServings,
        confidenceScore = confidenceScore,
    )
}

