package com.muhammad.nutribot.data.remote.repository

import com.muhammad.nutribot.data.remote.mapper.toFood
import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.domain.network.SearchFoodNetwork
import com.muhammad.nutribot.domain.repository.search_food.SearchFoodRespository
import com.muhammad.nutribot.utils.Result

class SearchFoodRespositoryImp(
    private val searchFoodNetwork: SearchFoodNetwork
) : SearchFoodRespository{
    override suspend fun searchFoods(query: String): Result<List<Food>> {
        val response =  searchFoodNetwork.searchFoods(query)
        return when(response){
            is Result.Error -> response
            is Result.Success -> {
                Result.Success(response.data.results.map { it.toFood() })
            }
        }
    }
}