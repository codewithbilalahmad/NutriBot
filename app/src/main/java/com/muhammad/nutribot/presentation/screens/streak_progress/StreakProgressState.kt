package com.muhammad.nutribot.presentation.screens.streak_progress

import com.muhammad.nutribot.domain.model.Food

data class StreakProgressState(
    val streak : Int = 0,
    val weekMeals : List<Food> = emptyList()
)
