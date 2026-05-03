package com.muhammad.nutribot.presentation.screens.nurition_setup

import com.muhammad.nutribot.R
import com.muhammad.nutribot.domain.model.NuritionSetupStep

data class NuritionSetupState(
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