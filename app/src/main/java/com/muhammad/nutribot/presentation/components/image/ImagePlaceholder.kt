package com.muhammad.nutribot.presentation.components.image

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.R
import com.muhammad.nutribot.utils.rippleClickable

@Composable
fun ImagePlaceholder(
    modifier: Modifier = Modifier,
    borderWidth : Dp = 5.dp,
    size: Dp = 100.dp,onClick : (() -> Unit)?=null,
    shape: Shape = RoundedCornerShape(16.dp),
) {
    val density = LocalDensity.current
    val translationX = with(density) { size.toPx() * 0.5f }
    val translationY = with(density) { -12.dp.toPx() }
    Box(modifier) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    rotationZ = -12f
                }
                .size(size)
                .clip(shape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        Box(
            modifier = Modifier
                .graphicsLayer {
                    rotationZ = 20f
                    this.translationX = translationX
                    this.translationY = translationY
                }
                .size(size)
                .border(width = borderWidth, color = MaterialTheme.colorScheme.background, shape = shape)
                .clip(shape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .then(if(onClick != null) Modifier.rippleClickable{
                    onClick()
                } else Modifier),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_dish),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.background,
                modifier = Modifier.size(size.times(0.7f))
            )
        }
    }
}