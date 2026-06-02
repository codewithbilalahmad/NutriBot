package com.muhammad.nutribot.presentation.screens.progress

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.muhammad.nutribot.presentation.navigation.Destination
import com.muhammad.nutribot.presentation.screens.progress.components.ProgressTopbar
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProgressScreen(
    navHostController: NavHostController,
    viewModel: ProgressViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        ProgressTopbar(streak = state.streak, onStreakClick = {
            navHostController.navigate(Destination.StreakScreen)
        }, onSettingClick = {})
    }){  pading}
}