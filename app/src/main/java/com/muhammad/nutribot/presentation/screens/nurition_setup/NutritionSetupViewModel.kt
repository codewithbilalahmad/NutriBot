package com.muhammad.nutribot.presentation.screens.nurition_setup

import androidx.lifecycle.ViewModel
import com.muhammad.nutribot.domain.model.ActivityLevel
import com.muhammad.nutribot.domain.model.Gender
import com.muhammad.nutribot.domain.model.MainGoal
import com.muhammad.nutribot.domain.repository.NutritionCalculationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NutritionSetupViewModel(
    private val nutritionCalculationRepository: NutritionCalculationRepository
) : ViewModel(){
    private val _state = MutableStateFlow(NutritionSetupState())
    val state = _state.asStateFlow()
    fun onAction(action : NutritionSetupAction){
        when(action){
            is NutritionSetupAction.OnChangeCurrentStep -> onChangeCurrentStep(action.isIncrement)
            is NutritionSetupAction.OnGenderSelected -> onGenderSelected(action.gender)
            is NutritionSetupAction.OnAgeSelected -> onAgeSelected(action.age)
            is NutritionSetupAction.OnHeightCmSelected -> onHeightCmSelected(action.heightCm)
            is NutritionSetupAction.OnWeightKgSelected -> onWeightKgSelected(action.weightKg)
            is NutritionSetupAction.OnActivityLevelSelected -> onActivityLevelSelected(action.activityLevel)
            is NutritionSetupAction.OnToggleMainGoalSelection -> onToggleMainGoalSelection(action.mainGoal)
            NutritionSetupAction.OnCalculateNutrition -> onCalculateNutrition()
        }
    }

    private fun onCalculateNutrition() {
        _state.update { currentState ->
            val nutritionCalculation = nutritionCalculationRepository.calculateNutrition(
                age = currentState.selectedAge,
                mainGoals = currentState.selectedMainGoals,
                activityLevel = currentState.selectedActivityLevel,
                gender = currentState.selectedGender,
                weightKg = currentState.selectedWeightKg,
                heightCm = currentState.selectedHeightCm
            )
            currentState.copy(nutritionCalculation = nutritionCalculation)
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