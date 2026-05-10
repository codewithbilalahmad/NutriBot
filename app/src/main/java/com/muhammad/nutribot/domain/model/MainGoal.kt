package com.muhammad.nutribot.domain.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.muhammad.nutribot.R

@Immutable
enum class MainGoal(
    val icon: String,
    @get:StringRes val label: Int,
    @get:StringRes val desp: Int
) {

    LOSE_WEIGHT(
        icon = "🔥",
        label = R.string.lose_weight,
        desp = R.string.lose_weight_desp
    ),

    GAIN_MUSCLE(
        icon = "💪",
        label = R.string.gain_muscle,
        desp = R.string.gain_muscle_desp
    ),

    MAINTAIN_WEIGHT(
        icon = "⚖️",
        label = R.string.maintain_weight,
        desp = R.string.maintain_weight_desp
    ),

    BOOST_ENERGY(
        icon = "⚡",
        label = R.string.boost_energy,
        desp = R.string.boost_energy_desp
    ),

    IMPROVE_NUTRITION(
        icon = "🥗",
        label = R.string.improve_nutrition,
        desp = R.string.improve_nutrition_desp
    ),

    GAIN_WEIGHT(
        icon = "🍽️",
        label = R.string.gain_weight,
        desp = R.string.gain_weight_desp
    ),
}