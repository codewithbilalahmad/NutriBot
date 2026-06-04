package com.muhammad.nutribot.data.remote.dto.barcode_meal

import kotlinx.serialization.*

@Serializable
data class BarcodeMealNutrimentsDto(
    @SerialName("energy-kcal_100g")
    val calories: Double? = null,

    @SerialName("proteins_100g")
    val protein: Double? = null,

    @SerialName("fat_100g")
    val fat: Double? = null,

    @SerialName("carbohydrates_100g")
    val carbs: Double? = null
)
