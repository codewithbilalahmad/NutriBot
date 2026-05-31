package com.muhammad.nutribot.data.remote.mapper

import com.muhammad.nutribot.data.remote.dto.food_search.FoodSearchDto
import com.muhammad.nutribot.domain.model.Food

fun FoodSearchDto.toFood() : Food{
    fun get(name : String) = nutrition.nutrients.find { it.name.equals(name, true) }?.amount?.toInt() ?: 0
    return Food(
        id = id,
        name = title,
        mealImageUrl = image,
        calories = get("Calories"),
        carbs = get("Carbohydrates"),
        fat = get("Fat"),
        protein = get("Protein"),
        ingredients = emptyList(), eatenAt = 0L, confidenceScore = 100, isFavorite = false
    )
}