package com.muhammad.nutribot.domain.repository.ingredient

import com.muhammad.nutribot.domain.model.Ingredient
import kotlinx.coroutines.flow.Flow

interface IngredientRepository {
    suspend fun upsertIngredient(ingredient: Ingredient)
    suspend fun updateIngredientSelection(id: Long, isSelected: Boolean)
    fun getIngredientByFoodId(foodId: Long): Flow<List<Ingredient>>
}