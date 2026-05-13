package com.muhammad.nutribot.presentation.screens.scan_meal.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.R
import com.muhammad.nutribot.utils.rippleClickable

@Composable
fun ScanMealButton(modifier: Modifier = Modifier, onClick: () -> Unit, enabled: Boolean) {
    val containerColor by animateColorAsState(
        targetValue = if (enabled) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(
            0.6f
        ), animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(), label = "containerColor"
    )
    val contentColor by animateColorAsState(
        targetValue = if (enabled) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceContainer,
        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
        label = "containerColor"
    )
    Box(
        modifier = modifier
            .size(75.dp)
            .clip(CircleShape)
            .border(
                color = containerColor,
                width = 2.dp,
                shape = CircleShape
            )
            .padding(5.dp)
            .background(color = containerColor, shape = CircleShape)
            .rippleClickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_scan),
            contentDescription = null,
            modifier = Modifier.size(35.dp),
            tint = contentColor
        )
    }
}