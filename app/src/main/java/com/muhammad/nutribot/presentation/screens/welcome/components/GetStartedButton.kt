package com.muhammad.nutribot.presentation.screens.welcome.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.R
import com.muhammad.nutribot.utils.rippleClickable

@Composable
fun GetStartedButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .rippleClickable(onClick = onClick)
            .border(
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.5f),
                width = 1.5.dp,
                shape = CircleShape
            )
            .padding(4.dp), contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.get_started),
            style = MaterialTheme.typography.bodyLarge
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(50.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
                .rippleClickable(onClick = onClick), contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}