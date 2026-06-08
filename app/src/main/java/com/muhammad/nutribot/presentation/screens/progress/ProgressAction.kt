package com.muhammad.nutribot.presentation.screens.progress

sealed interface ProgressAction{
    data object OnToggleAddFoodBottomSheet : ProgressAction
}