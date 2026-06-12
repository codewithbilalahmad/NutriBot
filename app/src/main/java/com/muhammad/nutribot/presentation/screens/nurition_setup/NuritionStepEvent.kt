package com.muhammad.nutribot.presentation.screens.nurition_setup

sealed interface NuritionStepEvent{
    data object OnSaveNuritionSuccess : NuritionStepEvent
}