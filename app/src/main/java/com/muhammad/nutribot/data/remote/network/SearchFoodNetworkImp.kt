package com.muhammad.nutribot.data.remote.network

import com.muhammad.nutribot.data.remote.dto.food_search.FoodSearchResponse
import com.muhammad.nutribot.data.remote.network.network.get
import com.muhammad.nutribot.domain.network.SearchFoodNetwork
import com.muhammad.nutribot.utils.Constants.FOOD_SEARCH_API_KEY
import com.muhammad.nutribot.utils.Constants.FOOD_SEARCH_BASE_URL
import com.muhammad.nutribot.utils.Result
import io.ktor.client.HttpClient

class SearchFoodNetworkImp(
    private val httpClient: HttpClient,
) : SearchFoodNetwork {
    override suspend fun searchFoods(query: String): Result<FoodSearchResponse> {
        return httpClient.get<FoodSearchResponse>(
            route = "${FOOD_SEARCH_BASE_URL}recipes/complexSearch", queryParameters = mapOf(
                "query" to query,
                "number" to 15,
                "fillIngredients" to true,
                "addRecipeNutrition" to true,
                "apiKey" to FOOD_SEARCH_API_KEY
            )
        )
    }
}