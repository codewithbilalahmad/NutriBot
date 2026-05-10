package com.muhammad.nutribot.presentation.screens.setting

sealed interface SettingAction{
    data object OnToggleGenderSection : SettingAction
    data object OnToggleAgeSection : SettingAction
    data object OnToggleHeightAndWeightSection : SettingAction
    data object OnToggleActivityLevelSection : SettingAction
    data class OnToggleReminderEnabled(val enable : Boolean) : SettingAction
}