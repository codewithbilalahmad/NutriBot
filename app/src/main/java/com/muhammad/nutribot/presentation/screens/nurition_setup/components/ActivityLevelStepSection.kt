package com.muhammad.nutribot.presentation.screens.nurition_setup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.domain.model.ActivityLevel

@Composable
fun ActivityLevelStepSection(
    modifier: Modifier = Modifier,
    selectedActivityLevel: ActivityLevel,
    onSelectActivityLevel: (ActivityLevel) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ActivityLevel.entries.forEach { activityLevel ->
            val isSelected = selectedActivityLevel == activityLevel
            ActivityLevelCard(
                activityLevel = activityLevel,
                isSelected = isSelected,
                onSelectActivityLevel = onSelectActivityLevel
            )
        }
    }
}