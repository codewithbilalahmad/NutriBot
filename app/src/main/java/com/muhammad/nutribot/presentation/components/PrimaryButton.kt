package com.muhammad.nutribot.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    containerColor : Color = MaterialTheme.colorScheme.primary,
    contentColor : Color = MaterialTheme.colorScheme.onPrimary,
    textStyle : TextStyle = MaterialTheme.typography.bodyLarge,
    enabled: Boolean = true, contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val containerColor by animateColorAsState(
        targetValue = if (enabled) containerColor else MaterialTheme.colorScheme.surfaceVariant,
        label = "containerColor", animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()
    )
    val contentColor by animateColorAsState(
        targetValue = if (enabled) contentColor else MaterialTheme.colorScheme.outline,
        label = "contentColor", animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()
    )
    Button(
        onClick = {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
            onClick()
        }, enabled = enabled, colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = contentColor
        ), modifier = modifier, shapes = ButtonDefaults.shapes(), contentPadding = contentPadding
    ) {
        leadingIcon?.invoke()
        if (leadingIcon != null) {
            Spacer(Modifier.width(6.dp))
        }
        Text(
            text = text,
            style = textStyle
        )
    }
}