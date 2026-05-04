package com.muhammad.nutribot.presentation.screens.nurition_setup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.domain.model.MainGoal

@Composable
fun MainGoalStepSection(
    modifier: Modifier = Modifier,
    selectedMainGoals : List<MainGoal>,
    onSelectMainGoal: (MainGoal) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MainGoal.entries.forEach { mainGoal->
            val isSelected = selectedMainGoals.contains(mainGoal)
            MainGoalCard(
                mainGoal = mainGoal,
                isSelected = isSelected,
                onSelectMainGoal = onSelectMainGoal
            )
        }
    }
}