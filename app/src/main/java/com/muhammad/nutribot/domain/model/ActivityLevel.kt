package com.muhammad.nutribot.domain.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.muhammad.nutribot.R

@Immutable
enum class ActivityLevel(
    val icon: String,
    @get:StringRes val label: Int
) {
    SEDENTARY(
        icon = "🛋️",
        label = R.string.sedentary
    ),
    LIGHTLY_ACTIVE(
        icon = "🚶",
        label = R.string.lightly_active
    ),
    MODERATELY_ACTIVE(
        icon = "🏃",
        label = R.string.moderately_active
    ),
    VERY_ACTIVE(
        icon = "🏋️",
        label = R.string.very_active
    ),
    SUPER_ACTIVE(
        icon = "🔥",
        label = R.string.super_active
    ),
}