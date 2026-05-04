package com.muhammad.nutribot.domain.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.muhammad.nutribot.R

@Immutable
enum class MainGoal(val icon: String, @get:StringRes val label: Int) {
    LOSE_WEIGHT(icon = "🔥", label = R.string.lose_weight),
    GAIN_MUSCLE(icon = "💪", label = R.string.gain_muscle),
    MAINTAIN_WEIGHT(icon = "⚖️", label = R.string.maintain_weight),
    BOOST_ENERGY(icon = "⚡", label = R.string.boost_energy),
    IMPROVE_NUTRITION(icon = "🥗", label = R.string.improve_nutrition),
    GAIN_WEIGHT(icon = "🍽️", label = R.string.gain_weight),
}