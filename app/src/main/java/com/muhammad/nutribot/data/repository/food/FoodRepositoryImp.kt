package com.muhammad.nutribot.data.repository.food

import com.muhammad.nutribot.data.local.dao.FoodDao
import com.muhammad.nutribot.data.local.dao.getFoodStreak
import com.muhammad.nutribot.data.local.mappers.toFood
import com.muhammad.nutribot.data.local.mappers.toFoodEntity
import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.domain.repository.food.FoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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
        return foodDao.getFoodsByDate(startOfDay, endOfDay).map { entities ->
            entities.map { entity -> entity.toFood() }
        }
    }

    override fun getAllFoods(): Flow<List<Food>> {
        return foodDao.getAllFoods().map { entities ->
            entities.map { entity -> entity.toFood() }
        }
    }

    override fun getFoodStreak(): Flow<Int> {
        return foodDao.getFoodStreak()
    }

    override fun getFoodById(id: Long): Flow<Food?> {
        return foodDao.getFoodById(id).map { it?.toFood() }
    }

    override fun updateFoodFavourite(id: Long, isFavourite: Boolean) {
        return foodDao.updateFoodFavourite(id = id, isFavourite = isFavourite)
    }
}