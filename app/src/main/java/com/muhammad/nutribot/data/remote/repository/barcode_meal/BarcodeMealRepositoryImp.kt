package com.muhammad.nutribot.data.remote.repository.barcode_meal

import com.muhammad.nutribot.data.remote.mapper.toFood
import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.domain.network.BarcodeMealNetwork
import com.muhammad.nutribot.domain.repository.barcode_meal.BarcodeMealRepository
import com.muhammad.nutribot.utils.Result

class BarcodeMealRepositoryImp(
    private val barcodeMealNetwork: BarcodeMealNetwork
) : BarcodeMealRepository {
    override suspend fun getBarcodeMeal(barcode: String): Result<Food?> {
        val response =  barcodeMealNetwork.getBarcodeMeal(barcode)
        return when(response){
            is Result.Error -> response
            is Result.Success-> {
                Result.Success(response.data.meal?.toFood())
            }
        }
    }
}