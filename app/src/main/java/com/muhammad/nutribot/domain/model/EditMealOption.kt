package com.muhammad.nutribot.domain.model

import androidx.compose.runtime.Immutable

@Immutable
enum class EditMealOption {
    NONE,
    NAME,
    CALORIES,
    PROTEIN,
    FATS,
    CARBS
}