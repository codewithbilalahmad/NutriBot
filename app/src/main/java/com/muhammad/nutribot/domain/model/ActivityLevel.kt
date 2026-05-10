package com.muhammad.nutribot.domain.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.muhammad.nutribot.R

@Immutable
enum class ActivityLevel(
    val icon: String,
    @get:StringRes val label: Int,
    @get:StringRes val desp: Int,
) {
    SEDENTARY(
        icon = "🛋️",
        label = R.string.sedentary,
        desp = R.string.sedentary_desp
    ),
    LIGHTLY_ACTIVE(
        icon = "🚶",
        label = R.string.lightly_active,
        desp = R.string.lightly_active_desp
    ),
    MODERATELY_ACTIVE(
        icon = "🏃",
        label = R.string.moderately_active,
        desp = R.string.moderately_active_desp
    ),
    VERY_ACTIVE(
        icon = "🏋️",
        label = R.string.very_active,
        desp = R.string.very_active_desp
    ),
    SUPER_ACTIVE(
        icon = "🔥",
        label = R.string.super_active,
        desp = R.string.super_active_desp
    ),
}