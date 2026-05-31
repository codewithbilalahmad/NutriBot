package com.muhammad.nutribot.data.local.mappers

import com.muhammad.nutribot.data.local.entity.HistoryFoodEntity
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