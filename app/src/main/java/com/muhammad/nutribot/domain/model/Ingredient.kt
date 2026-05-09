package com.muhammad.nutribot.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Ingredient(
    val id: Long = 0L,
    val foodId : Long,
    val name: String,
    val calories : Int,
    val protein : Int,
    val fat : Int,
    val carbs : Int,
    val isSelected : Boolean
)