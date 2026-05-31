package com.muhammad.nutribot.data.remote.dto.food_search

import kotlinx.serialization.Serializable

@Serializable
data class NutritionDto(
    val nutrients: List<NutrientDto>,
    val weightPerServing: WeightPerServingDto?=null
)