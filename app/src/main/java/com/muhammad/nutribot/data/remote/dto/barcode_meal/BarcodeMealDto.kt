package com.muhammad.nutribot.data.remote.dto.barcode_meal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BarcodeMealDto(
    @SerialName("product_name")
    val productName: String? = null,

    @SerialName("image_url")
    val imageUrl: String? = null,

    @SerialName("serving_size")
    val servingSize: String? = null,

    @SerialName("nutriments")
    val nutriments: BarcodeMealNutrimentsDto? = null
)