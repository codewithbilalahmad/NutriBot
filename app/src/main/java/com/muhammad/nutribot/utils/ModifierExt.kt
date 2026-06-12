package com.muhammad.nutribot.utils

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.rippleClickable(enabled: Boolean = true, onClick: () -> Unit): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
        label = "scale"
    )
    return this.graphicsLayer {
        scaleX = scale
        scaleY = scale
    }.pointerInput(Unit){
        awaitEachGesture {
            awaitFirstDown(requireUnconsumed = false)
            isPressed = true
            waitForUpOrCancellation()
            isPressed = false
        }
    }.clickable(
        enabled = enabled,
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick
    )
}

@Composable
fun Modifier.loadingEffect(
    loadingColors : List<Color> = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(0.6f),
        MaterialTheme.colorScheme.surfaceVariant.copy(0.3f),
        MaterialTheme.colorScheme.surfaceVariant.copy(0.6f)
    )
) : Modifier{
    val infiniteTransition = rememberInfiniteTransition("loadingEffect")
    val offsetX by infiniteTransition.animateFloat(
        initialValue = -200f, targetValue = 800f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "loadingEffect"
    )
    return drawBehind {
        val brush = Brush.linearGradient(
            colors = loadingColors,
            start = Offset(x = offsetX, y = 0f),
            end = Offset(offsetX + size.width, size.height)
        )
        drawRoundRect(brush = brush)
    }
}

@Composable
fun Modifier.circleBubbles() : Modifier{
    val infiniteTransition = rememberInfiniteTransition()
    val circleColor = MaterialTheme.colorScheme.surfaceContainer.copy(0.6f)
    val smallCircleRadius by infiniteTransition.animateFloat(
        initialValue = 0.4f, targetValue = 0.45f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "smallCircleRadius"
    )
    val bigCircleRadius by infiniteTransition.animateFloat(
        initialValue = 0.5f, targetValue = 0.55f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "bigCircleRadius"
    )
    return this.drawBehind {
        drawCircle(
            color = circleColor,
            radius = size.width * smallCircleRadius,
            center = Offset(x = size.width * 0.8f, y = size.height * 0.2f)
        )
        drawCircle(
            color = circleColor,
            radius = size.width * bigCircleRadius,
            center = Offset(x = size.width * 0.1f, y = size.height * 0.9f)
        )
    }
}

@Composable
fun Modifier.dashedBorder(
    brush: Brush,
    shape: Shape,
    strokeWidth: Dp = 1.5.dp,
    dashedLength: Dp = 10.dp,
    gapLength: Dp = 5.dp,
): Modifier {
    val density = LocalDensity.current
    val dashedLengthPx = with(density){
        dashedLength.toPx()
    }
    val gapLengthPx =with(density){
        gapLength.toPx()
    }
    val infiniteTransition = rememberInfiniteTransition("phase")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = dashedLengthPx + gapLengthPx,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "phase"
    )
    return this.drawWithContent {
        val outlined = shape.createOutline(size = size, layoutDirection = layoutDirection, density = this)
        val dashedStroke = Stroke(
            width = strokeWidth.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round,
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(dashedLengthPx, gapLengthPx), phase = phase
            )
        )
        drawContent()
        drawOutline(outline = outlined, style = dashedStroke, brush = brush)
    }
}