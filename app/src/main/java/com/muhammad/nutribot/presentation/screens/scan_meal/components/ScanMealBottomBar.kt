package com.muhammad.nutribot.presentation.screens.scan_meal.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.R

@Composable
fun ScanMealBottomBar(
    modifier: Modifier = Modifier,
    isFlashOn: Boolean,
    enabled: Boolean,
    onCaptureMealPhoto: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(start = 24.dp, end = 24.dp, bottom = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = {},
            modifier = Modifier.size(IconButtonDefaults.mediumContainerSize()),
            shapes = IconButtonDefaults.shapes(),
            enabled = enabled,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.background.copy(0.2f),
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_gallery),
                contentDescription = null
            )
        }
        ScanMealButton(onClick = onCaptureMealPhoto, enabled = enabled)
        IconButton(
            onClick = {},
            modifier = Modifier.size(IconButtonDefaults.mediumContainerSize()),
            enabled = enabled,
            shapes = IconButtonDefaults.shapes(),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.background.copy(0.2f),
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ) {
            val icon = if (isFlashOn) R.drawable.ic_flash else R.drawable.ic_flash_off
            Icon(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = null
            )
        }
    }
}