package com.muhammad.nutribot.domain.repository.barcode_meal

import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.utils.Result

interface BarcodeMealRepository {
    suspend fun getBarcodeMeal(barcode: String) : Result<Food?>
}