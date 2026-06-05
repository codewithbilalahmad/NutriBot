package com.muhammad.nutribot.data.remote.mapper

import com.muhammad.nutribot.data.remote.dto.barcode_meal.BarcodeMealDto
import com.muhammad.nutribot.domain.model.Food

fun BarcodeMealDto.toFood(): Food {
    return Food(
        id = 0L,
        name = productName ?: "Unknown Food",
        servingSize = servingSize ?: "100g",
        mealImageUrl = imageUrl.orEmpty(),
        calories = nutriments?.calories?.toInt() ?: 0,
        protein = nutriments?.protein?.toInt() ?: 0,
        fat = nutriments?.fat?.toInt() ?: 0,
        carbs = nutriments?.carbs?.toInt() ?: 0,
        numberOfServings = 1,
        eatenAt = 0L,
        confidenceScore = 100,
        ingredients = emptyList(),
        isFavorite = false
    )
}