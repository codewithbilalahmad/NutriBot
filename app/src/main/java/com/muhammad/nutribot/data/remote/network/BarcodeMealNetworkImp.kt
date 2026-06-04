package com.muhammad.nutribot.data.remote.network

import com.muhammad.nutribot.data.remote.dto.barcode_meal.BarcodeMealResponse
import com.muhammad.nutribot.data.remote.network.network.get
import com.muhammad.nutribot.domain.network.BarcodeMealNetwork
import com.muhammad.nutribot.utils.Constants.BARCODE_MEAL_BASE_URL
import com.muhammad.nutribot.utils.Result
import io.ktor.client.HttpClient

class BarcodeMealNetworkImp(
    private val httpClient: HttpClient
) : BarcodeMealNetwork {
    override suspend fun getBarcodeMeal(barcode: String): Result<BarcodeMealResponse> {
        return httpClient.get<BarcodeMealResponse>("${BARCODE_MEAL_BASE_URL}product/$barcode")
    }
}