package com.muhammad.nutribot.domain.network

import com.muhammad.nutribot.data.remote.dto.food_search.FoodSearchResponse
import com.muhammad.nutribot.utils.Result

interface SearchFoodNetwork {
    suspend fun searchFoods(query : String) : Result<FoodSearchResponse>
}