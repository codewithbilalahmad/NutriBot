package com.muhammad.nutribot.data.local.mappers

import com.muhammad.nutribot.data.local.entity.HistoryFoodEntity
import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.domain.model.HistoryFood

fun HistoryFoodEntity.toHistoryFood(): HistoryFood {
    return HistoryFood(
        id = id,
        name = name,
        createdAt = createdAt,
        calories = calories,
        mealImageUrl = mealImageUrl,
        carbs = calories,
        numberOfServings = numberOfServings,
        servingSize = servingSize,
        protein = protein,
        fat = fat,
    )
}

fun HistoryFood.toHistoryFoodEntity(): HistoryFoodEntity {
    return HistoryFoodEntity(
        id = id,
        name = name,
        createdAt = createdAt,
        calories = calories,
        mealImageUrl = mealImageUrl,
        carbs = calories,
        fat = fat,
        numberOfServings = numberOfServings,
        protein = protein,
        servingSize = servingSize,
    )
}

fun HistoryFood.toFood(): Food {
    return Food(
        id = id,
        name = name,
        eatenAt = createdAt,
        calories = calories,
        mealImageUrl = mealImageUrl,
        carbs = calories,
        confidenceScore = 100,
        ingredients = emptyList(),
        isFavorite = false,
        numberOfServings = numberOfServings,
        servingSize = servingSize,
        protein = protein,
        fat = fat,
    )
}