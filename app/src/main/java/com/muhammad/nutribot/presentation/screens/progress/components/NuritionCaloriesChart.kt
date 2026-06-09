package com.muhammad.nutribot.presentation.screens.progress.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.muhammad.nutribot.R
import com.muhammad.nutribot.domain.model.NutritionCaloriesPoint
import com.muhammad.nutribot.presentation.theme.CarbsColor
import com.muhammad.nutribot.presentation.theme.FatColor
import com.muhammad.nutribot.presentation.theme.ProteinColor

@Composable
fun NuritionCaloriesChart(
    modifier: Modifier = Modifier,
    points: List<NutritionCaloriesPoint>,
    graphLineColor: Color = MaterialTheme.colorScheme.surface,
    goalCaloriesContainerColor: Color = MaterialTheme.colorScheme.error,
    goalCaloriesContentColor: Color = MaterialTheme.colorScheme.onError,
    targetCalories: Int,
    labelFontSize: TextUnit = MaterialTheme.typography.bodySmall.fontSize,
    dashedLineColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    labelColor: Color = MaterialTheme.colorScheme.onBackground,
    unit: String = stringResource(R.string.kcal)
) {
    val context = LocalContext.current
    val rows = 9

    val maxCalories = maxOf(
        targetCalories.toFloat(),
        points.maxOfOrNull { it.totalCalories.toFloat() } ?: 0f
    )

    val yMax = ((maxCalories * 1.2f) / 100f).toInt() * 100f
    val stepValue = yMax / rows
    val yLabels = (0..rows).map { (it * stepValue).toInt() }

    val latoTypeface = ResourcesCompat.getFont(context, R.font.lato)

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(320.dp)
    ) {
        if (points.isEmpty()) return@Canvas

        val yLabelPaint = Paint().apply {
            color = labelColor.toArgb()
            textSize = labelFontSize.toPx()
            typeface = latoTypeface
            textAlign = Paint.Align.RIGHT
            isAntiAlias = true
        }

        val xLabelPaint = Paint().apply {
            color = labelColor.toArgb()
            textSize = labelFontSize.toPx()
            typeface = latoTypeface
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }

        val widestYLabelWidth = yLabels.maxOf { yLabelPaint.measureText("$it") }

        val leftPadding = widestYLabelWidth + 48f
        val rightPadding = 16f
        val topPadding = 16f
        val bottomPadding = 56f

        val chartWidth = size.width - leftPadding - rightPadding
        val chartHeight = size.height - topPadding - bottomPadding
        val xAxisY = topPadding + chartHeight

        val rowHeight = chartHeight / rows

        val slotWidth = chartWidth / points.size.coerceAtLeast(1)

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

        yLabels.forEachIndexed { i, value ->
            val y = xAxisY - (i * rowHeight)

            drawLine(
                color = dashedLineColor,
                start = Offset(leftPadding, y),
                end = Offset(leftPadding + chartWidth, y),
                strokeWidth = 2f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
            )

            drawLine(
                color = labelColor.copy(alpha = 0.6f),
                start = Offset(leftPadding - 8f, y),
                end = Offset(leftPadding, y),
                strokeWidth = 2f
            )

            val textOffset = (yLabelPaint.ascent() + yLabelPaint.descent()) / 2f

            drawContext.canvas.nativeCanvas.drawText(
                "$value",
                leftPadding - 12f,
                y - textOffset,
                yLabelPaint
            )
        }

        points.forEachIndexed { i, point ->
            val x = leftPadding + slotWidth * i + slotWidth / 2f

            drawLine(
                color = dashedLineColor,
                start = Offset(x, topPadding),
                end = Offset(x, xAxisY),
                strokeWidth = 2f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f))
            )

            drawLine(
                color = labelColor.copy(alpha = 0.6f),
                start = Offset(x, xAxisY),
                end = Offset(x, xAxisY + 8f),
                strokeWidth = 2f
            )

            val dayName = point.date.dayOfWeek.name
                .take(3)
                .lowercase()
                .replaceFirstChar { it.uppercase() }

            val dayNameY = xAxisY + 40f
            val dayNumberY = dayNameY + xLabelPaint.textSize + 8f

            drawContext.canvas.nativeCanvas.drawText(
                dayName,
                x,
                dayNameY,
                xLabelPaint
            )

            drawContext.canvas.nativeCanvas.drawText(
                point.date.day.toString(),
                x,
                dayNumberY,
                xLabelPaint
            )
        }

        val barWidth = 30f
        val bottom = xAxisY

        points.forEachIndexed { i, point ->

            val x = leftPadding + slotWidth * i + slotWidth / 2f - barWidth / 2f

            val totalBarHeight =
                (point.totalCalories.toFloat() / yMax) * chartHeight

            val macroCalories =
                point.proteinCalories +
                        point.carbCalories +
                        point.fatCalories

            val proteinPercent =
                if (macroCalories > 0)
                    point.proteinCalories.toFloat() / macroCalories
                else 0f

            val carbPercent =
                if (macroCalories > 0)
                    point.carbCalories.toFloat() / macroCalories
                else 0f

            val fatPercent =
                if (macroCalories > 0)
                    point.fatCalories.toFloat() / macroCalories
                else 0f

            val proteinH = totalBarHeight * proteinPercent
            val carbH = totalBarHeight * carbPercent
            val fatH = totalBarHeight * fatPercent

            if (proteinH > 0f) {
                drawRect(
                    color = ProteinColor,
                    topLeft = Offset(
                        x,
                        bottom - proteinH
                    ),
                    size = Size(
                        barWidth,
                        proteinH
                    )
                )
            }

            if (carbH > 0f) {
                drawRect(
                    color = CarbsColor,
                    topLeft = Offset(
                        x,
                        bottom - proteinH - carbH
                    ),
                    size = Size(
                        barWidth,
                        carbH
                    )
                )
            }

            if (fatH > 0f) {

                val fatTop =
                    bottom - proteinH - carbH - fatH

                val radius = minOf(
                    18f,
                    fatH / 2f,
                    barWidth / 2f
                )

                val fatPath = Path().apply {
                    moveTo(x, fatTop + fatH)
                    lineTo(x, fatTop + radius)

                    quadraticTo(
                        x,
                        fatTop,
                        x + radius,
                        fatTop
                    )

                    lineTo(
                        x + barWidth - radius,
                        fatTop
                    )

                    quadraticTo(
                        x + barWidth,
                        fatTop,
                        x + barWidth,
                        fatTop + radius
                    )

                    lineTo(
                        x + barWidth,
                        fatTop + fatH
                    )

                    close()
                }

                drawPath(
                    color = FatColor,
                    path = fatPath
                )
            }
        }

        val goalY = xAxisY - (targetCalories / yMax) * chartHeight

        drawLine(
            color = Color(0xFFE76F51),
            start = Offset(leftPadding, goalY),
            end = Offset(leftPadding + chartWidth, goalY),
            strokeWidth = 3f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 10f))
        )

        val goalPaint = Paint().apply {
            color = goalCaloriesContentColor.toArgb()
            textAlign = Paint.Align.CENTER
            textSize = labelFontSize.toPx()
            typeface = latoTypeface
            isAntiAlias = true
        }

        val goalText1 = "Goal"
        val goalText2 = "$targetCalories $unit"

        val goalWidth = maxOf(
            goalPaint.measureText(goalText1),
            goalPaint.measureText(goalText2)
        ) + 32f

        val goalHeight = 70f
        val goalX = (size.width - goalWidth - 8f).coerceAtLeast(0f)

        drawRoundRect(
            color = goalCaloriesContainerColor,
            topLeft = Offset(goalX, goalY - goalHeight / 2),
            size = Size(goalWidth, goalHeight),
            cornerRadius = CornerRadius(10f)
        )

        drawContext.canvas.nativeCanvas.drawText(
            goalText1,
            goalX + goalWidth / 2,
            goalY - 6f,
            goalPaint
        )

        drawContext.canvas.nativeCanvas.drawText(
            goalText2,
            goalX + goalWidth / 2,
            goalY + 20f,
            goalPaint
        )
    }
}