package com.muhammad.nutribot.presentation.screens.meal_details.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.muhammad.nutribot.R
import com.muhammad.nutribot.domain.model.LikeParticle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun HeartLikeAnimation(
    modifier: Modifier = Modifier,
    showAnimation: Boolean,
    onAnimationFinished: () -> Unit,
) {
    val particles = remember {
        List(12) { index ->
            LikeParticle(angle = index * 30f)
        }
    }
    val heartScale = remember { Animatable(0f) }
    val heartAlpha = remember { Animatable(1f) }
    LaunchedEffect(showAnimation) {
        if (showAnimation) {
            heartScale.snapTo(0f)
            heartAlpha.snapTo(1f)
            launch {
                heartScale.animateTo(
                    targetValue = 1.4f,
                    animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
                )
                heartScale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
                )
            }
            launch {
                delay(500)
                heartAlpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 300, easing = LinearEasing)
                )
            }
        }
    }
    if (showAnimation) {
        Box(modifier = modifier.size(140.dp), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_favourite_filled),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = heartScale.value
                        scaleY = heartScale.value
                        alpha = heartAlpha.value
                    }
                    .size(120.dp)
            )
            particles.forEach { particle ->
                HeartParticleItem(
                    particle = particle,
                    alpha = heartAlpha.value,
                    onAnimationFinished = onAnimationFinished
                )
            }
        }
    }
}

@Composable
fun HeartParticleItem(particle: LikeParticle, alpha: Float, onAnimationFinished: () -> Unit) {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        progress.snapTo(0f)
        launch {
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 700,
                    easing = FastOutSlowInEasing
                )
            )
            onAnimationFinished()
        }
    }
    val radius = 220f
    val radians = Math.toRadians(particle.angle.toDouble())
    val targetX = cos(radians).toFloat() * radius
    val targetY = sin(radians).toFloat() * radius
    val currentX = targetX * progress.value
    val currentY = targetY * progress.value
    Box(
        modifier = Modifier
            .graphicsLayer {
                this.alpha = alpha
            }
            .offset {
                IntOffset(
                    currentX.toInt(),
                    currentY.toInt()
                )
            }
            .size(
                lerp(
                    start = 8.dp,
                    stop = 4.dp,
                    fraction = progress.value
                )
            )
            .background(
                color = MaterialTheme.colorScheme.error,
                shape = CircleShape
            )
    )
}