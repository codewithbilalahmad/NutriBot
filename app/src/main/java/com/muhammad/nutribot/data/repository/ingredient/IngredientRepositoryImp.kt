package com.muhammad.nutribot.data.repository.ingredient

import com.muhammad.nutribot.data.local.dao.IngredientDao
import com.muhammad.nutribot.data.local.mappers.toIngredient
import com.muhammad.nutribot.data.local.mappers.toIngredientEntity
import com.muhammad.nutribot.domain.model.Ingredient
import com.muhammad.nutribot.domain.repository.ingredient.IngredientRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class IngredientRepositoryImp(
    private val ingredientDao: IngredientDao
) : IngredientRepository{
    override suspend fun upsertIngredient(ingredient: Ingredient) {
        ingredientDao.upsertIngredient(ingredient.toIngredientEntity())
    }

    override suspend fun updateIngredientSelection(id: Long, isSelected: Boolean) {
        ingredientDao.updateIngredientSelection(id, isSelected)
    }

    override fun getIngredientByFoodId(foodId: Long): Flow<List<Ingredient>> {
        return ingredientDao.getIngredientByFoodId(foodId).map { entities ->
            entities.map { entity -> entity.toIngredient() }
        }
    }
}