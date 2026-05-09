package com.muhammad.nutribot.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable
    data object WelcomeScreen : Destination
    @Serializable
    data object BoardingScreen : Destination
    @Serializable
    data object NutritionSetupScreen : Destination
    @Serializable
    data object DiaryScreen : Destination
    @Serializable
    data object ProgressScreen : Destination
    @Serializable
    data object SettingScreen : Destination
}