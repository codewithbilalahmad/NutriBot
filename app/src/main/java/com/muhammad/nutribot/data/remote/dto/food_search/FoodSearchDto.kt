package com.muhammad.nutribot.data.remote.dto.food_search

import kotlinx.serialization.Serializable

@Serializable
data class FoodSearchDto(
    val id : Long,
    val title : String,
    val image : String,
    val nutrition : NutritionDto
)