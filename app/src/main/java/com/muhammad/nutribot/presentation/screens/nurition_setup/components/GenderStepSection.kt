package com.muhammad.nutribot.presentation.screens.nurition_setup.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.domain.model.Gender
import com.muhammad.nutribot.utils.rippleClickable

@Composable
fun GenderStepSection(
    modifier: Modifier = Modifier,
    selectedGender: Gender,
    onGenderSelected: (Gender) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                40.dp,
                Alignment.CenterHorizontally
            )
        ) {
            Gender.entries.take(2).forEach { gender ->
                val isSelected = selectedGender == gender
                GenderItem(
                    gender = gender,
                    isSelected = isSelected,
                    onGenderSelected = onGenderSelected
                )
            }
        }
        PreferNotToSayItem(
            isSelected = selectedGender == Gender.PREFER_NOT_TO_SAY,
            onGenderSelected = onGenderSelected
        )
    }
}

@Composable
fun PreferNotToSayItem(
    modifier: Modifier = Modifier,
    gender: Gender = Gender.PREFER_NOT_TO_SAY,
    isSelected: Boolean, onGenderSelected: (Gender) -> Unit,
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
        label = "borderColor"
    )
    val borderWidth by animateDpAsState(
        targetValue = if (isSelected) 2.dp else 1.dp,
        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
        label = "borderColor"
    )
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
        label = "containerColor"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
        label = "contentColor"
    )
    Box(
        modifier = modifier
            .clip(CircleShape)
            .rippleClickable {
                onGenderSelected(gender)
            }
            .border(width = borderWidth, color = borderColor, shape = CircleShape)
            .background(containerColor)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(gender.value),
            style = MaterialTheme.typography.bodyLarge.copy(color = contentColor)
        )
    }
}