package com.muhammad.nutribot.domain.repository.food_history

import com.muhammad.nutribot.domain.model.HistoryFood
import kotlinx.coroutines.flow.Flow

interface FoodHistoryRespository {
    suspend fun upsertHistoryFood(historyFood: HistoryFood)
    fun getAllHistoryFoods(): Flow<List<HistoryFood>>
}