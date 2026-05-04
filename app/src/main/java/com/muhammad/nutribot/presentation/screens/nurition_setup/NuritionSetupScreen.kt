package com.muhammad.nutribot.presentation.screens.nurition_setup

import androidx.compose.animation.AnimatedContent
import com.muhammad.nutribot.R
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.muhammad.nutribot.presentation.components.textfield.AppTextField
import com.muhammad.nutribot.presentation.components.wheel_picker.WheelPicker
import com.muhammad.nutribot.presentation.screens.nurition_setup.components.ActivityLevelStepSection
import com.muhammad.nutribot.presentation.screens.nurition_setup.components.GenderStepSection
import com.muhammad.nutribot.presentation.screens.nurition_setup.components.HeightStepSection
import com.muhammad.nutribot.presentation.screens.nurition_setup.components.MainGoalStepSection
import com.muhammad.nutribot.presentation.screens.nurition_setup.components.NuritionSetupBottomBar
import com.muhammad.nutribot.presentation.screens.nurition_setup.components.NuritionSetupTopbar
import com.muhammad.nutribot.presentation.screens.nurition_setup.components.WeightStepSection
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NuritionSetupScreen(
    navHostController: NavHostController,
    viewModel: NuritionSetupViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        NuritionSetupTopbar(
            modifier = Modifier.fillMaxWidth(),
            currentStep = state.currentStep,
            currentStepIndex = state.currentStepIndex,
            totalSteps = state.steps.size, onPreviousStepClick = {
                if (state.currentStepIndex == 0) {
                    navHostController.navigateUp()
                } else {
                    viewModel.onAction(NuritionSetupAction.OnChangeCurrentStep(isIncrement = false))
                }
            }
        )
    }, bottomBar = {
        NuritionSetupBottomBar(modifier = Modifier.fillMaxWidth(), onNextStepClick = {
            viewModel.onAction(NuritionSetupAction.OnChangeCurrentStep(isIncrement = true))
        })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedContent(
                targetState = state.currentStepIndex,
                transitionSpec = { fadeIn() togetherWith fadeOut() }) { currentStep ->
                when (currentStep) {
                    0 -> {
                        AppTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 32.dp),
                            state = state.username,
                            hint = R.string.username
                        )
                    }

                    1 -> {
                        GenderStepSection(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            selectedGender = state.selectedGender,
                            onGenderSelected = { gender ->
                                viewModel.onAction(NuritionSetupAction.OnGenderSelected(gender))
                            })
                    }

                    2 -> {
                        WheelPicker(
                            range = 1..100,
                            onItemSelected = { age ->
                                viewModel.onAction(NuritionSetupAction.OnAgeSelected(age))
                            },
                            initialValue = 50,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                        )
                    }

                    3 -> {
                        HeightStepSection(
                            selectedHeightCm = state.selectedHeightCm,
                            onHeightSelected = { heightCm ->
                                viewModel.onAction(NuritionSetupAction.OnHeightCmSelected(heightCm))
                            })
                    }

                    4 -> {
                        WeightStepSection(
                            selectedWeightKg = state.selectedWeightKg,
                            onWeightSelected = { weightCm ->
                                viewModel.onAction(NuritionSetupAction.OnWeightKgSelected(weightCm))
                            })
                    }

                    5 -> {
                        ActivityLevelStepSection(
                            modifier = Modifier.fillMaxWidth(),
                            onSelectActivityLevel = { activityLevel ->
                                viewModel.onAction(
                                    NuritionSetupAction.OnActivityLevelSelected(
                                        activityLevel
                                    )
                                )
                            },
                            selectedActivityLevel = state.selectedActivityLevel
                        )
                    }

                    6 -> {
                        MainGoalStepSection(
                            modifier = Modifier.fillMaxWidth(),
                            selectedMainGoals = state.selectedMainGoals,
                            onSelectMainGoal = { goal ->
                                viewModel.onAction(NuritionSetupAction.OnToggleMainGoalSelection(goal))
                            }
                        )
                    }
                }
            }
        }
    }
}