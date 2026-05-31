package com.muhammad.nutribot.domain.repository.search_food

import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.utils.Result

interface SearchFoodRespository{
    suspend fun searchFoods(query : String) : Result<List<Food>>
}