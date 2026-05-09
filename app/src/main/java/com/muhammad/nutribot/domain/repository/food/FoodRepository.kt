package com.muhammad.nutribot.domain.repository.food

import com.muhammad.nutribot.domain.model.Food
import kotlinx.coroutines.flow.Flow


interface FoodRepository {
    suspend fun upsertFood(food: Food)
    suspend fun deleteFood(id: Long)
    fun getFoodsByDate(
        startOfDay: Long,
        endOfDay: Long,
    ): Flow<List<Food>>

    fun getFoodStreak(): Flow<Int>
    fun getFoodById(id: Long): Flow<Food?>
}