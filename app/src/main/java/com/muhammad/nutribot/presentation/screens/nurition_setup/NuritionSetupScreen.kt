package com.muhammad.nutribot.presentation.screens.nurition_setup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.muhammad.nutribot.presentation.screens.nurition_setup.components.NuritionSetupBottomBar
import com.muhammad.nutribot.presentation.screens.nurition_setup.components.NuritionSetupTopbar
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
                if(state.currentStepIndex == 0){
                    navHostController.navigateUp()
                } else{
                    viewModel.onAction(NuritionSetupAction.OnChangeCurrentStep(isIncrement = false))
                }
            }
        )
    }, bottomBar = {
        NuritionSetupBottomBar(modifier = Modifier.fillMaxWidth(), onNextStepClick = {
            viewModel.onAction(NuritionSetupAction.OnChangeCurrentStep(isIncrement = true))
        })
    }) {paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)){  }
    }

}