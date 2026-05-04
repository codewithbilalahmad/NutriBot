package com.muhammad.nutribot.presentation.screens.nurition_setup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.domain.model.Gender

@Composable
fun GenderStepSection(
    modifier: Modifier = Modifier,
    selectedGender: Gender,
    onGenderSelected: (Gender) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            40.dp,
            Alignment.CenterHorizontally
        )
    ) {
        Gender.entries.forEach { gender ->
            val isSelected = selectedGender == gender
            GenderItem(
                gender = gender,
                isSelected = isSelected,
                onGenderSelected = onGenderSelected
            )
        }
    }
}