package com.muhammad.nutribot.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.muhammad.nutribot.data.local.entity.HistoryFoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryFoodDao {
    @Query("SELECT * FROM HistoryFoodEntity ORDER BY createdAt DESC")
    fun getAllHistoryFoods(): Flow<List<HistoryFoodEntity>>
}