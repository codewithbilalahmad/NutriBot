package com.muhammad.nutribot.presentation.screens.progress.components

import android.graphics.Paint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.muhammad.nutribot.R
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.muhammad.nutribot.domain.model.NuritionChartPoint
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.ceil
import kotlin.time.Clock

@Composable
fun NutritionLineChart(
    modifier: Modifier = Modifier,
    points: List<NuritionChartPoint>,
    dashedLineColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    graphLineColor: Color = MaterialTheme.colorScheme.surface,
    labelBorderColor: Color = MaterialTheme.colorScheme.surface,
    labelFontSize: TextUnit = MaterialTheme.typography.bodySmall.fontSize,
    labelColor: Color = MaterialTheme.colorScheme.onBackground,
    unit: String = stringResource(R.string.grams),
) {
    val context = LocalContext.current
    val today = stringResource(R.string.today)
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val progress = remember { Animatable(0f) }
    LaunchedEffect(points) {
        progress.snapTo(0f)
        progress.animateTo(1f, tween(durationMillis = 1000, easing = FastOutSlowInEasing))
    }
    val maxValue = points.maxOfOrNull { it.value } ?: 0
    val hasData = maxValue > 0

    val labelCount = 6

    val maxChartValue = if (hasData) {
        val roundedMax = ceil(maxValue / 5f) * 5f
        roundedMax.coerceAtLeast(5f)
    } else {
        5f
    }

    val stepValue = maxChartValue / (labelCount - 1)

    val yLabels = (0 until labelCount).map {
        (it * stepValue).toInt()
    }

    val latoTypeface = ResourcesCompat.getFont(context, R.font.lato)
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
    ) {
        val yLabelPaint = Paint().apply {
            color = labelColor.toArgb()
            textSize = labelFontSize.toPx()
            isAntiAlias = true
            textAlign = Paint.Align.RIGHT
            typeface = latoTypeface
        }

        val xLabelPaint = Paint().apply {
            color = labelColor.toArgb()
            textSize = labelFontSize.toPx()
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            typeface = latoTypeface
        }
        val widestYLabelWidth = yLabels.maxOf { yLabelPaint.measureText("$it $unit") }

        val fm = xLabelPaint.fontMetrics
        val xLabelHeight = fm.descent - fm.ascent


        val leftPadding = widestYLabelWidth + 48f
        val rightPadding = 24f
        val topPadding = 36f
        val bottomPadding = xLabelHeight + 40f

        val chartWidth = size.width - leftPadding - rightPadding
        val chartHeight = size.height - topPadding - bottomPadding

        val xAxisY = topPadding + chartHeight

        val colWidth = if (points.isEmpty()) 0f else chartWidth / points.size

        fun valueToY(v: Int): Float {
            return xAxisY - (v.toFloat() / maxChartValue) * chartHeight
        }
        drawLine(
            color = graphLineColor,
            start = Offset(leftPadding, topPadding),
            end = Offset(leftPadding, xAxisY),
            strokeWidth = 3f
        )

        drawLine(
            color = graphLineColor,
            start = Offset(leftPadding, xAxisY),
            end = Offset(leftPadding + chartWidth, xAxisY),
            strokeWidth = 3f
        )


        yLabels.forEachIndexed { i, label ->
            val ratio = i.toFloat() / (labelCount - 1)
            val y = xAxisY - ratio * chartHeight

            drawLine(
                color = dashedLineColor,
                start = Offset(leftPadding, y),
                end = Offset(leftPadding + chartWidth, y),
                strokeWidth = 2f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
            )

            drawLine(
                color = labelBorderColor,
                start = Offset(leftPadding - 8f, y),
                end = Offset(leftPadding, y),
                strokeWidth = 2f,
            )

            val textCentreOffset = (yLabelPaint.ascent() + yLabelPaint.descent()) / 2f
            drawContext.canvas.nativeCanvas.drawText(
                "$label $unit",
                leftPadding - 12f,
                y - textCentreOffset,
                yLabelPaint,
            )
        }


        val chartPoints = if (points.isNotEmpty()) points.mapIndexed { index, point ->

            val cx = leftPadding + colWidth * index + colWidth / 2f
            val cy = valueToY(point.value)

            drawLine(
                color = dashedLineColor,
                start = Offset(cx, topPadding),
                end = Offset(cx, xAxisY),
                strokeWidth = 2f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f)),
            )

            drawLine(
                color = labelBorderColor,
                start = Offset(cx, xAxisY),
                end = Offset(cx, xAxisY + 8f),
                strokeWidth = 2f
            )

            val dayName = if(point.date == now) today else  point.date.dayOfWeek.name
                .lowercase()
                .take(3)
                .replaceFirstChar { it.uppercase() }

            val dayNumber = point.date.day.toString()

            val dayNameY = xAxisY + 40f
            val dayNumberY = dayNameY + xLabelPaint.textSize + 8f

            drawContext.canvas.nativeCanvas.drawText(
                dayName,
                cx,
                dayNameY,
                xLabelPaint
            )

            drawContext.canvas.nativeCanvas.drawText(
                dayNumber,
                cx,
                dayNumberY,
                xLabelPaint
            )

            Offset(cx, cy)
        } else emptyList()


        val linePath = buildSmoothPath(chartPoints)

        if (chartPoints.isNotEmpty()) {

            val fillPath = Path().apply {
                addPath(linePath)
                lineTo(chartPoints.last().x, xAxisY)
                lineTo(chartPoints.first().x, xAxisY)
                close()
            }

            clipRect(right = size.width * progress.value) {

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            lineColor.copy(alpha = 0.35f),
                            Color.Transparent
                        ),
                        startY = topPadding,
                        endY = xAxisY,
                    ),
                )

                drawPath(
                    path = linePath,
                    color = lineColor.copy(alpha = 0.18f),
                    style = Stroke(width = 22f, cap = StrokeCap.Round),
                )

                drawPath(
                    path = linePath,
                    color = lineColor,
                    style = Stroke(width = 8f, cap = StrokeCap.Round),
                )

                chartPoints.forEachIndexed { index, p ->
                    val isToday = points[index].date == now
                    val outerR = if (isToday) 16f else 12f
                    val innerR = if (isToday) 8f else 6f

                    drawCircle(
                        color = lineColor,
                        radius = outerR,
                        center = p
                    )

                    drawCircle(
                        color = Color.White,
                        radius = innerR,
                        center = p
                    )
                }
            }
        }
    }
}

private fun buildSmoothPath(points: List<Offset>): Path {
    val path = Path()
    if (points.isEmpty()) return path

    path.moveTo(points.first().x, points.first().y)

    for (i in 1 until points.size) {
        val prev = points[i - 1]
        val curr = points[i]

        val cp1 = Offset((prev.x + curr.x) / 2f, prev.y)
        val cp2 = Offset((prev.x + curr.x) / 2f, curr.y)

        path.cubicTo(cp1.x, cp1.y, cp2.x, cp2.y, curr.x, curr.y)
    }

    return path
}