package com.muhammad.nutribot.data.local.mappers

import com.muhammad.nutribot.data.local.entity.FoodEntity
import com.muhammad.nutribot.data.local.relations.FoodWithWithIngredients
import com.muhammad.nutribot.domain.model.Food

fun FoodWithWithIngredients.toFood(): Food {
    return Food(
        id = food.id,
        name = food.name,
        calories = food.calories,
        fat = food.fat,
        servingSize = food.servingSize,
        protein = food.protein,
        mealImageUrl = food.mealImageUrl,
        carbs = food.carbs,
        eatenAt = food.eatenAt,
        ingredients = ingredients.map { it.toIngredient() },
        numberOfServings = food.numberOfServings,
        confidenceScore = food.confidenceScore,
        isFavorite = food.isFavourite
    )
}

fun FoodEntity.toFood(): Food {
    return Food(
        id = id,
        name = name,
        calories = calories,
        fat = fat,
        servingSize = servingSize,
        protein = protein,
        carbs = carbs,
        eatenAt = eatenAt,
        mealImageUrl = mealImageUrl,
        ingredients = emptyList(),
        confidenceScore = confidenceScore,
        numberOfServings = numberOfServings,
        isFavorite = isFavourite
    )
}

fun Food.toFoodEntity(): FoodEntity {
    return FoodEntity(
        id = id,
        name = name,
        servingSize = servingSize,
        mealImageUrl = mealImageUrl,
        calories = calories,
        fat = fat,
        protein = protein,
        carbs = carbs,
        eatenAt = eatenAt,
        numberOfServings = numberOfServings,
        confidenceScore = confidenceScore,
        isFavourite = isFavorite
    )
}

