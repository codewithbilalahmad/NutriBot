package com.muhammad.nutribot.presentation.screens.nurition_setup

sealed interface NuritionSetupAction{
    data class OnChangeCurrentStep(val isIncrement : Boolean) : NuritionSetupAction
}