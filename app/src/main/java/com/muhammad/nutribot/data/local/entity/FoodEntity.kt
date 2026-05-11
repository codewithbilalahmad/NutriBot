package com.muhammad.nutribot.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0L,
    val name : String,
    val calories : Int,
    val protein : Int,
    val fat : Int,
    val carbs : Int,
    val numberOfServings : Int,
    val confidenceScore : Int,
    val eatenAt : Long = System.currentTimeMillis()
)