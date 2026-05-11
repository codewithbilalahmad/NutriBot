package com.muhammad.nutribot.presentation.screens.scan_meal.components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.R

@Composable
fun CameraPermissionCard(
    modifier: Modifier = Modifier,
    cameraPermissionGranted: Boolean,
    onRequestCameraPermission: () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(width = 1.5.dp, color = MaterialTheme.colorScheme.surfaceVariant),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!cameraPermissionGranted) {
                PermissionRow(
                    icon = R.drawable.ic_camera,
                    spacing = 8.dp,
                    onClick = onRequestCameraPermission,
                    label = R.string.access_camera
                )
            }
        }
    }
}

@Composable
private fun PermissionRow(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    spacing: Dp,
    icon: Int,
    @StringRes label: Int,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(
            spacing,
            Alignment.CenterHorizontally
        ), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(icon),
            contentDescription = null, tint = MaterialTheme.colorScheme.surface,
            modifier = Modifier.size(26.dp)
        )
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,color = MaterialTheme.colorScheme.surface
            )
        )
    }
}