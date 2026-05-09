package com.muhammad.nutribot.data.local.relations

import androidx.room.*
import com.muhammad.nutribot.data.local.entity.FoodEntity
import com.muhammad.nutribot.data.local.entity.IngredientEntity

data class FoodWithWithIngredients(
    @Embedded
    val food : FoodEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "foodId"
    )
    val ingredients : List<IngredientEntity>
)