package com.muhammad.nutribot.presentation.screens.streak

sealed interface StreakAction{
    data object OnSaveBestStreak : StreakAction
}