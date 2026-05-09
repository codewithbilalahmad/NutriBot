package com.muhammad.nutribot.presentation.screens.nurition_setup

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.muhammad.nutribot.R
import com.muhammad.nutribot.domain.model.Nutrition
import com.muhammad.nutribot.presentation.components.textfield.AppTextField
import com.muhammad.nutribot.presentation.components.wheel_picker.WheelPicker
import com.muhammad.nutribot.presentation.screens.nurition_setup.components.ActivityLevelStepSection
import com.muhammad.nutribot.presentation.screens.nurition_setup.components.GenderStepSection
import com.muhammad.nutribot.presentation.screens.nurition_setup.components.HeightStepSection
import com.muhammad.nutribot.presentation.screens.nurition_setup.components.MainGoalStepSection
import com.muhammad.nutribot.presentation.screens.nurition_setup.components.NuritionSetupBottomBar
import com.muhammad.nutribot.presentation.screens.nurition_setup.components.NuritionSetupTopbar
import com.muhammad.nutribot.presentation.screens.nurition_setup.components.NutritionDonutChart
import com.muhammad.nutribot.presentation.screens.nurition_setup.components.NutritionItem
import com.muhammad.nutribot.presentation.screens.nurition_setup.components.WeightStepSection
import com.muhammad.nutribot.presentation.theme.CarbsColor
import com.muhammad.nutribot.presentation.theme.FatColor
import com.muhammad.nutribot.presentation.theme.ProteinColor
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NutritionSetupScreen(
    navHostController: NavHostController,
    viewModel: NutritionSetupViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    BackHandler {
        when{
            state.nutritionCalculation != null ->{
                viewModel.onAction(NutritionSetupAction.OnResetNutritionData)
            }
            state.currentStepIndex > 0 ->{
                viewModel.onAction(NutritionSetupAction.OnChangeCurrentStep(isIncrement = false))
            }
            else ->{
                navHostController.navigateUp()
            }
        }
    }
    AnimatedContent(targetState = state.nutritionCalculation != null, transitionSpec = {
        fadeIn() togetherWith fadeOut()
    }) { showNuritionCalculation ->
        if (showNuritionCalculation) {
            Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    IconButton(
                        onClick = {
                            viewModel.onAction(NutritionSetupAction.OnResetNutritionData)
                        },
                        modifier = Modifier.padding(horizontal = 8.dp),
                        shapes = IconButtonDefaults.shapes()
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left),
                            contentDescription = null
                        )
                    }
                    Text(
                        text = stringResource(R.string.calories_plan_ready),
                        maxLines = 2,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                            .padding(horizontal = 24.dp),
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center)
                    )
                }
            }, bottomBar = {
                NuritionSetupBottomBar(
                    modifier = Modifier.fillMaxWidth(),
                    onNextStepClick = {
                        viewModel.onAction(NutritionSetupAction.OnStartNutritionPlan)
                    },
                    isContinueEnable = true, label = R.string.start_your_plan_now
                )
            }) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    state.nutritionCalculation?.let { nutritionCalculation ->
                        NutritionDonutChart(
                            calories = nutritionCalculation.calories,
                            modifier = Modifier.size(250.dp),
                            nutrition = listOf(
                                Nutrition(
                                    label = R.string.carbs,
                                    percentage = state.nutritionCalculation!!.carbsPercent,
                                    color = CarbsColor
                                ),
                                Nutrition(
                                    label = R.string.protein,
                                    percentage = state.nutritionCalculation!!.proteinPercent,
                                    color = ProteinColor
                                ),
                                Nutrition(
                                    label = R.string.fat,
                                    percentage = state.nutritionCalculation!!.fatPercent,
                                    color = FatColor
                                )

                            )
                        )
                    }
                    Spacer(Modifier.height(50.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(0.7f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        NutritionItem(label = R.string.carbs, color = CarbsColor)
                        NutritionItem(label = R.string.protein, color = ProteinColor)
                        NutritionItem(label = R.string.fat, color = FatColor)
                    }
                }
            }
        } else {
            Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                NuritionSetupTopbar(
                    modifier = Modifier.fillMaxWidth(),
                    currentStep = state.currentStep,
                    currentStepIndex = state.currentStepIndex,
                    totalSteps = state.steps.size, onPreviousStepClick = {
                        if (state.currentStepIndex == 0) {
                            navHostController.navigateUp()
                        } else {
                            viewModel.onAction(NutritionSetupAction.OnChangeCurrentStep(isIncrement = false))
                        }
                    }
                )
            }, bottomBar = {
                NuritionSetupBottomBar(
                    modifier = Modifier.fillMaxWidth(), onNextStepClick = {
                        if (state.currentStepIndex == 6) {
                            viewModel.onAction(NutritionSetupAction.OnCalculateNutrition)
                        } else {
                            viewModel.onAction(NutritionSetupAction.OnChangeCurrentStep(isIncrement = true))
                        }
                    }, isContinueEnable = when (state.currentStepIndex) {
                        0 -> state.username.text.isNotEmpty()
                        6 -> state.selectedMainGoals.isNotEmpty()
                        else -> true
                    }
                )
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
                                    contentPadding = PaddingValues(
                                        horizontal = 24.dp,
                                        vertical = 32.dp
                                    ),
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
                                        viewModel.onAction(
                                            NutritionSetupAction.OnGenderSelected(
                                                gender
                                            )
                                        )
                                    })
                            }

                            2 -> {
                                WheelPicker(
                                    range = 1..100,
                                    onItemSelected = { age ->
                                        viewModel.onAction(NutritionSetupAction.OnAgeSelected(age))
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
                                        viewModel.onAction(
                                            NutritionSetupAction.OnHeightCmSelected(
                                                heightCm
                                            )
                                        )
                                    })
                            }

                            4 -> {
                                WeightStepSection(
                                    selectedWeightKg = state.selectedWeightKg,
                                    onWeightSelected = { weightCm ->
                                        viewModel.onAction(
                                            NutritionSetupAction.OnWeightKgSelected(
                                                weightCm
                                            )
                                        )
                                    })
                            }

                            5 -> {
                                ActivityLevelStepSection(
                                    modifier = Modifier.fillMaxWidth(),
                                    onSelectActivityLevel = { activityLevel ->
                                        viewModel.onAction(
                                            NutritionSetupAction.OnActivityLevelSelected(
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
                                        viewModel.onAction(
                                            NutritionSetupAction.OnToggleMainGoalSelection(
                                                goal
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}