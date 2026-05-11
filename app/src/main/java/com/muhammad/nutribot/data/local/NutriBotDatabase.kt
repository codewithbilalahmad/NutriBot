package com.muhammad.nutribot.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.muhammad.nutribot.data.local.dao.FoodDao
import com.muhammad.nutribot.data.local.dao.IngredientDao
import com.muhammad.nutribot.data.local.entity.FoodEntity
import com.muhammad.nutribot.data.local.entity.IngredientEntity

@Database(
    entities = [
        FoodEntity::class,
        IngredientEntity::class
    ],
    exportSchema = true,
    version = 2
)
abstract class NutriBotDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun ingredientDao(): IngredientDao
}