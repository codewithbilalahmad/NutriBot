package com.muhammad.nutribot.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryFoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0L,
    val name : String,
    val calories : Int,
    val servingSize : String = "",
    val mealImageUrl : String,
    val protein : Int,
    val fat : Int,
    val carbs : Int,
    val numberOfServings : Int,
    val createdAt : Long = System.currentTimeMillis()
)