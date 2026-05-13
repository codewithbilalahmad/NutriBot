package com.muhammad.nutribot.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Ingredient(
    val id: Long,
    val foodId : Long,
    val name: String,
    val calories : Int,
    val protein : Int,
    val fat : Int,
    val carbs : Int,
    val isSelected : Boolean= true
)