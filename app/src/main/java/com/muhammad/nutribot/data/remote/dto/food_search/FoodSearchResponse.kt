package com.muhammad.nutribot.data.remote.dto.food_search

import kotlinx.serialization.Serializable

@Serializable
data class FoodSearchResponse(
    val results: List<FoodSearchDto>
)