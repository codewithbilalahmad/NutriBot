package com.muhammad.nutribot.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class Food(
    val id : Long,
    val name : String,
    val servingSize : String = "",
    val mealImageUrl : String = "",
    val calories : Int,
    val protein : Int,
    val fat : Int,
    val carbs : Int,
    val numberOfServings : Int = 1,
    val eatenAt : Long,
    val confidenceScore : Int,
    val ingredients: List<Ingredient>,
    val isFavorite : Boolean
)