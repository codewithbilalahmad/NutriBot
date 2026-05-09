package com.muhammad.nutribot.data.local.mappers

import com.muhammad.nutribot.data.local.entity.IngredientEntity
import com.muhammad.nutribot.domain.model.Ingredient

fun IngredientEntity.toIngredient() : Ingredient{
    return Ingredient(
        id = id,
        name = name,
        calories = calories,
        protein = protein,
        fat = fat,
        carbs = carbs,
        foodId = foodId,
        isSelected = isSelected
    )
}

fun Ingredient.toIngredientEntity() : IngredientEntity{
    return IngredientEntity(
        id = id,
        name = name,
        calories = calories,
        protein = protein,
        fat = fat,
        carbs = carbs,
        foodId = foodId,
        isSelected = isSelected
    )
}