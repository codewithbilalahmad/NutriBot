package com.muhammad.nutribot.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class ScannedIngredient(
    val name: String = "",
    val calories: Int = 0,
    val protein: Int = 0,
    val fat: Int = 0,
    val carbs: Int = 0,
    val isSelected: Boolean = true
)