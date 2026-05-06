package com.muhammad.nutribot.presentation.screens.nurition_setup

import com.muhammad.nutribot.domain.model.ActivityLevel
import com.muhammad.nutribot.domain.model.Gender
import com.muhammad.nutribot.domain.model.MainGoal

sealed interface NutritionSetupAction{
    data class OnChangeCurrentStep(val isIncrement : Boolean) : NutritionSetupAction
    data class OnGenderSelected(val gender : Gender) : NutritionSetupAction
    data object OnResetNutritionData : NutritionSetupAction
    data class OnAgeSelected(val age : Int) : NutritionSetupAction
    data class OnHeightCmSelected(val heightCm : Int) : NutritionSetupAction
    data class OnWeightKgSelected(val weightKg : Int) : NutritionSetupAction
    data class OnActivityLevelSelected(val activityLevel: ActivityLevel) : NutritionSetupAction
    data class OnToggleMainGoalSelection(val mainGoal: MainGoal) : NutritionSetupAction
    data object OnCalculateNutrition : NutritionSetupAction
}