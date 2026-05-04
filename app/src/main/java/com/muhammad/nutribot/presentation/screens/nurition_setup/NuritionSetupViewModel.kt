package com.muhammad.nutribot.presentation.screens.nurition_setup

import androidx.lifecycle.ViewModel
import com.muhammad.nutribot.domain.model.ActivityLevel
import com.muhammad.nutribot.domain.model.Gender
import com.muhammad.nutribot.domain.model.MainGoal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NuritionSetupViewModel : ViewModel(){
    private val _state = MutableStateFlow(NuritionSetupState())
    val state = _state.asStateFlow()
    fun onAction(action : NuritionSetupAction){
        when(action){
            is NuritionSetupAction.OnChangeCurrentStep -> onChangeCurrentStep(action.isIncrement)
            is NuritionSetupAction.OnGenderSelected -> onGenderSelected(action.gender)
            is NuritionSetupAction.OnAgeSelected -> onAgeSelected(action.age)
            is NuritionSetupAction.OnHeightCmSelected -> onHeightCmSelected(action.heightCm)
            is NuritionSetupAction.OnWeightKgSelected -> onWeightKgSelected(action.weightKg)
            is NuritionSetupAction.OnActivityLevelSelected -> onActivityLevelSelected(action.activityLevel)
            is NuritionSetupAction.OnToggleMainGoalSelection -> onToggleMainGoalSelection(action.mainGoal)
        }
    }

    private fun onToggleMainGoalSelection(mainGoal: MainGoal) {
        _state.update {currentState ->
            val updatedMainGoals = if(currentState.selectedMainGoals.contains(mainGoal)){
                currentState.selectedMainGoals - mainGoal
            } else{
                currentState.selectedMainGoals + mainGoal
            }
            currentState.copy(selectedMainGoals = updatedMainGoals)
        }
    }

    private fun onActivityLevelSelected(activityLevel: ActivityLevel) {
        _state.update { it.copy(selectedActivityLevel = activityLevel) }
    }

    private fun onWeightKgSelected(weightKg: Int) {
        _state.update { it.copy(selectedWeightKg = weightKg) }
    }

    private fun onHeightCmSelected(heightCm: Int) {
        _state.update { it.copy(selectedHeightCm = heightCm) }
    }

    private fun onAgeSelected(age: Int) {
        _state.update { it.copy(selectedAge = age) }
    }

    private fun onGenderSelected(gender: Gender) {
        _state.update { it.copy(selectedGender = gender) }
    }

    private fun onChangeCurrentStep(increment: Boolean) {
        _state.update {
            it.copy(currentStepIndex = if(increment) it.currentStepIndex + 1 else it.currentStepIndex - 1)
        }
    }
}