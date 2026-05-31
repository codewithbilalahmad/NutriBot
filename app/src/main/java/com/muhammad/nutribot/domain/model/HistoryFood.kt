package com.muhammad.nutribot.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class HistoryFood(
    val id : Long,
    val name : String,
    val calories : Int,
    val servingSize : String,
    val mealImageUrl : String,
    val protein : Int,
    val fat : Int,
    val carbs : Int,
    val numberOfServings : Int,
    val createdAt : Long = System.currentTimeMillis()
)