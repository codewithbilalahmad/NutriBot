package com.muhammad.nutribot.data.repository.food_history

import com.muhammad.nutribot.data.local.dao.HistoryFoodDao
import com.muhammad.nutribot.data.local.mappers.toHistoryFood
import com.muhammad.nutribot.domain.model.HistoryFood
import com.muhammad.nutribot.domain.repository.food_history.FoodHistoryRespository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FoodHistoryRespositoryImp(
    private val foodHistoryFoodDao: HistoryFoodDao
) : FoodHistoryRespository {
    override fun getAllHistoryFoods(): Flow<List<HistoryFood>> {
        return foodHistoryFoodDao.getAllHistoryFoods().map { entities ->
            entities.map { it.toHistoryFood() }
        }
    }
}