package com.muhammad.nutribot.presentation.screens.nurition_setup

import androidx.compose.foundation.text.input.TextFieldState
import com.muhammad.nutribot.R
import com.muhammad.nutribot.domain.model.ActivityLevel
import com.muhammad.nutribot.domain.model.Gender
import com.muhammad.nutribot.domain.model.MainGoal
import com.muhammad.nutribot.domain.model.NuritionSetupStep

data class NuritionSetupState(
    val selectedGender : Gender = Gender.MALE,
    val username : TextFieldState = TextFieldState(),
    val selectedAge : Int = 50,
    val selectedHeightCm : Int = 180,
    val selectedWeightKg : Int = 60,
    val selectedMainGoals : List<MainGoal> = emptyList(),
    val selectedActivityLevel : ActivityLevel = ActivityLevel.SEDENTARY,
    val steps : List<NuritionSetupStep> = listOf(
        NuritionSetupStep(label = R.string.what_your_name),
        NuritionSetupStep(label = R.string.what_your_gender),
        NuritionSetupStep(label = R.string.what_your_age),
        NuritionSetupStep(label = R.string.how_tall_are_you),
        NuritionSetupStep(label = R.string.what_is_your_weight),
        NuritionSetupStep(label = R.string.what_is_your_activity),
        NuritionSetupStep(label = R.string.what_is_your_goal),
    ),
    val currentStepIndex : Int = 0,
){
    val currentStep = steps[currentStepIndex]
}