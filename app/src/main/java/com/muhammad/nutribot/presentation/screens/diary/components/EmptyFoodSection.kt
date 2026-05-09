package com.muhammad.nutribot.presentation.screens.diary.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.muhammad.nutribot.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun EmptyFoodSection(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 32f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "offset"
    )
    Column(
        modifier = modifier.padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.no_food_added),
            style = MaterialTheme.typography.titleSmall.copy(textAlign = TextAlign.Center)
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.add_today_meals),
            style = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.surface)
        )
        Spacer(Modifier.height(12.dp))
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_double_arrow_down),
            contentDescription = null,
            modifier = Modifier
                .size(35.dp)
                .graphicsLayer {
                    translationY = offset
                },
            tint = MaterialTheme.colorScheme.primary
        )
    }
}