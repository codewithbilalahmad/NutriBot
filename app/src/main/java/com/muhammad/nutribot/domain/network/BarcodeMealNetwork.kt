package com.muhammad.nutribot.domain.network

import com.muhammad.nutribot.data.remote.dto.barcode_meal.BarcodeMealResponse
import com.muhammad.nutribot.utils.Result


interface BarcodeMealNetwork {
    suspend fun getBarcodeMeal(barcode: String): Result<BarcodeMealResponse>
}