package com.muhammad.nutribot.presentation.screens.scan_meal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
    Box(
        modifier = modifier
            .size(75.dp)
            .clip(CircleShape)
            .border(
                color = MaterialTheme.colorScheme.onBackground,
                width = 2.dp,
                shape = CircleShape
            )
            .padding(5.dp)
            .background(color = MaterialTheme.colorScheme.onBackground, shape = CircleShape)
            .rippleClickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_scan),
            contentDescription = null,
            modifier = Modifier.size(35.dp),
            tint = MaterialTheme.colorScheme.surface
        )
    }
}