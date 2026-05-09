package com.muhammad.nutribot.data.local.entity

import androidx.room.*

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = FoodEntity::class,
            parentColumns = ["id"],
            childColumns = ["foodId"],
            onDelete = ForeignKey.CASCADE
        )
    ], indices = [Index("foodId")]
)
data class IngredientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val foodId : Long,
    val name: String,
    val calories : Int,
    val protein : Int,
    val fat : Int,
    val carbs : Int,
    val isSelected : Boolean
)
