package com.muhammad.nutribot.data.remote.dto.barcode_meal

import kotlinx.serialization.*

@Serializable
data class BarcodeMealResponse(
    @SerialName("product")
    val meal : BarcodeMealDto?
)
