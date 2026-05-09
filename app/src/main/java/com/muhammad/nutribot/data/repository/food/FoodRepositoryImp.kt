package com.muhammad.nutribot.data.repository.food

import com.muhammad.nutribot.data.local.dao.FoodDao
import com.muhammad.nutribot.data.local.dao.getFoodStreak
import com.muhammad.nutribot.data.local.mappers.toFood
import com.muhammad.nutribot.data.local.mappers.toFoodEntity
import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.domain.repository.food.FoodRepository
import com.muhammad.nutribot.utils.endOdDayMillis
import com.muhammad.nutribot.utils.startOfDayMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class FoodRepositoryImp(
    private val foodDao: FoodDao,
) : FoodRepository {
    override suspend fun upsertFood(food: Food) {
        foodDao.upsertFood(food.toFoodEntity())
    }

    override suspend fun deleteFood(id: Long) {
        foodDao.deleteFoodById(id)
    }

    override fun getFoodsByDate(
        startOfDay: Long,
        endOfDay: Long,
    ): Flow<List<Food>> {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val startOfDay = today.startOfDayMillis()
        val endOfDay = today.endOdDayMillis()
        return foodDao.getFoodsByDate(startOfDay, endOfDay).map { entities ->
            entities.map { entity -> entity.toFood() }
        }
    }

    override fun getFoodStreak(): Flow<Int> {
        return foodDao.getFoodStreak()
    }

    override fun getFoodById(id: Long): Flow<Food?> {
        return foodDao.getFoodById(id).map { it?.toFood() }
    }
}