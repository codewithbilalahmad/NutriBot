package com.muhammad.nutribot.presentation.screens.scan_meal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MealCorneredBox(
    modifier: Modifier = Modifier,
    cornerColor: Color = MaterialTheme.colorScheme.onBackground,
    containerColor: Color = Color.Transparent,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    strokeWidth: Dp = 4.dp,
    cornerLength: Dp = 80.dp,
    cornerRadius: Dp = 80.dp,
    shape: Shape = RoundedCornerShape(24.dp),
    content: @Composable () -> Unit = {},
) {

    Box(
        modifier = modifier
            .background(containerColor, shape)
            .drawBehind {

                val stroke = strokeWidth.toPx()
                val length = cornerLength.toPx()
                val radius = cornerRadius.toPx()
                fun drawCorner(
                    startX: Float,
                    startY: Float,
                    horizontalToRight: Boolean,
                    verticalToBottom: Boolean,
                ) {
                    val hDir = if (horizontalToRight) 1 else -1
                    val vDir = if (verticalToBottom) 1 else -1
                    val path = Path().apply {
                        moveTo(
                            x = startX + (length * hDir),
                            y = startY
                        )
                        lineTo(
                            x = startX + (radius * hDir),
                            y = startY
                        )
                        quadraticTo(
                            startX,
                            startY,
                            startX,
                            startY + (radius * vDir)
                        )
                        lineTo(
                            x = startX,
                            y = startY + (radius * vDir)
                        )
                    }
                    drawPath(
                        path = path, color = cornerColor, style = Stroke(
                            width = stroke, cap = StrokeCap.Round
                        )
                    )
                }
                drawCorner(
                    startX = 0f, startY = 0f, horizontalToRight = true, verticalToBottom = true
                )
                drawCorner(
                    startX = size.width,
                    startY = 0f,
                    horizontalToRight = false,
                    verticalToBottom = true
                )
                drawCorner(
                    startX = 0f,
                    startY = size.height,
                    horizontalToRight = true,
                    verticalToBottom = false
                )
                drawCorner(
                    startX = size.width,
                    startY = size.height,
                    horizontalToRight = false,
                    verticalToBottom = false
                )
            }
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}