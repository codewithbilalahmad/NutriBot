package com.muhammad.nutribot.presentation.screens.nurition_setup

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NuritionSetupViewModel : ViewModel(){
    private val _state = MutableStateFlow(NuritionSetupState())
    val state = _state.asStateFlow()
    fun onAction(action : NuritionSetupAction){
        when(action){
            is NuritionSetupAction.OnChangeCurrentStep -> onChangeCurrentStep(action.isIncrement)
        }
    }

    private fun onChangeCurrentStep(increment: Boolean) {
        _state.update {
            it.copy(currentStepIndex = if(increment) it.currentStepIndex + 1 else it.currentStepIndex - 1)
        }
    }
}