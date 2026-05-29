package com.muhammad.nutribot.presentation.screens.scan_meal.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.R

@Composable
fun Sparkles(
    modifier: Modifier = Modifier,
    size : Dp,
    color : Color = MaterialTheme.colorScheme.primary
) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 720f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 3000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )
    Box(modifier = modifier.size(size)) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_sparkle),
            contentDescription = null,
            tint = color,
            modifier = Modifier
                .graphicsLayer{
                    rotationZ = rotation
                }
                .size(size * 0.6f)
                .align(Alignment.TopEnd)
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_sparkle),
            contentDescription = null,
            tint = color,
            modifier = Modifier
                .graphicsLayer{
                    rotationZ = rotation
                }
                .size(size * 0.4f)
                .align(Alignment.CenterStart)
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_sparkle),
            contentDescription = null,
            tint = color,
            modifier = Modifier
                .graphicsLayer{
                    rotationZ = rotation
                }
                .size(size * 0.3f)
                .align(Alignment.BottomCenter)
        )
    }
}

@Preview
@Composable
private fun SparklesPreview() {
    Sparkles(size = 30.dp)
}

