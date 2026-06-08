package com.muhammad.nutribot.presentation.components.dashed_divider

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DashedHorizontalDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
    thickness: Dp = 2.dp, dashLength: Dp = 10.dp, gapLength: Dp = 5.dp,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val density = LocalDensity.current
    val thicknessPx = with(density) {
        thickness.toPx()
    }
    val gapLengthPx = with(density) {
        gapLength.toPx()
    }
    val dashLengthPx = with(density) {
        dashLength.toPx()
    }
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = dashLengthPx + gapLengthPx,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "phase"
    )
    Canvas(modifier = modifier) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            cap = StrokeCap.Round,
            strokeWidth = thicknessPx, pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(dashLengthPx, gapLengthPx),
                phase = phase
            )
        )
    }
}