package com.muhammad.nutribot.presentation.screens.streak

import com.muhammad.nutribot.domain.model.Food

data class StreakState(
    val streak : Int = 0,
    val bestStreak : Int = 0,
    val weekMeals : List<Food> = emptyList()
)