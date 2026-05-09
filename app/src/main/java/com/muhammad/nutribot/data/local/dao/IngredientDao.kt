package com.muhammad.nutribot.data.local.dao

import androidx.room.*
import com.muhammad.nutribot.data.local.entity.IngredientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {
    @Upsert
    suspend fun upsertIngredient(ingredient: IngredientEntity)
    @Query(
        "UPDATE IngredientEntity SET isSelected = :isSelected WHERE id = :id"
    )
    suspend fun updateIngredientSelection(id: Long, isSelected: Boolean)
    @Query(
        "SELECT * FROM IngredientEntity WHERE foodId=:foodId"
    )
    fun getIngredientByFoodId(foodId: Long): Flow<List<IngredientEntity>>
}