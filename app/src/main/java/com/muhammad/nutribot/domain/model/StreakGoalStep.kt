package com.muhammad.nutribot.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Shape

@Immutable
data class StreakGoalStep(
    val step : Int,
    val shape : Shape
)
