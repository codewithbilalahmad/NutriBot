package com.muhammad.nutribot.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.muhammad.nutribot.data.local.entity.HistoryFoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryFoodDao {
    @Upsert
    suspend fun upsertHistoryFood(historyFoodEntity: HistoryFoodEntity)
    @Query("SELECT * FROM HistoryFoodEntity ORDER BY createdAt DESC")
    fun getAllHistoryFoods(): Flow<List<HistoryFoodEntity>>
}