package com.muhammad.nutribot.utils

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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput

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