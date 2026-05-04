package com.muhammad.nutribot.presentation.screens.nurition_setup

import com.muhammad.nutribot.domain.model.ActivityLevel
import com.muhammad.nutribot.domain.model.Gender
import com.muhammad.nutribot.domain.model.MainGoal

sealed interface NuritionSetupAction{
    data class OnChangeCurrentStep(val isIncrement : Boolean) : NuritionSetupAction
    data class OnGenderSelected(val gender : Gender) : NuritionSetupAction
    data class OnAgeSelected(val age : Int) : NuritionSetupAction
    data class OnHeightCmSelected(val heightCm : Int) : NuritionSetupAction
    data class OnWeightKgSelected(val weightKg : Int) : NuritionSetupAction
    data class OnActivityLevelSelected(val activityLevel: ActivityLevel) : NuritionSetupAction
    data class OnToggleMainGoalSelection(val mainGoal: MainGoal) : NuritionSetupAction
}