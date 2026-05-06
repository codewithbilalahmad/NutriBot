package com.muhammad.nutribot.domain.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class Nutrition(
    @get:StringRes val label: Int,
    val percentage : Int,
    val color : Color
)
