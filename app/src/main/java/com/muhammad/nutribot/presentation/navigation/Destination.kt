package com.muhammad.nutribot.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable
    data object WelcomeScreen : Destination
    @Serializable
    data object BoardingScreen : Destination
    @Serializable
    data object NuritionSetupScreen : Destination
}