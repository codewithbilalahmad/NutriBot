package com.muhammad.nutribot.presentation.components.measurement_picker

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.R
import com.muhammad.nutribot.presentation.theme.lato
import kotlin.math.roundToInt

@Composable
fun MeasurementPicker(
    modifier: Modifier = Modifier,
    showOverlay: Boolean = false,
    overlayColor: Color = MaterialTheme.colorScheme.background,
    tickColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    majorTickColor: Color = MaterialTheme.colorScheme.surface,
    selectedTickColor: Color = MaterialTheme.colorScheme.primary,
    selectedLabelColor: Color = MaterialTheme.colorScheme.primary,
    unSelectedLabelColor: Color = MaterialTheme.colorScheme.surface,
    minMeasurement: Int,
    maxMeasurement: Int,
    tickSpacing: Dp = 16.dp,
    initialMeasurement: Int,
    onMeasurementChange: (Int) -> Unit,
){
    val hapticFeedback = LocalHapticFeedback.current
    val resolver = LocalFontFamilyResolver.current
    val density = LocalDensity.current
    var pickerWidth by remember { mutableIntStateOf(0) }
    val tickSpacingPx = with(density) { tickSpacing.toPx() }
    val totalTicks = maxMeasurement - minMeasurement
    val scrollState = rememberScrollState(
        initial = ((initialMeasurement - minMeasurement) * tickSpacingPx).toInt()
    )
    val scrollStateValue by remember { derivedStateOf { scrollState.value } }
    val selectedIndex by remember {
        derivedStateOf {
            (scrollStateValue / tickSpacingPx).roundToInt()
        }
    }
    val regularTypeface = remember {
        resolver.resolve(
            fontFamily = lato,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            fontSynthesis = FontSynthesis.All
        ).value as Typeface
    }

    val boldTypeface = remember {
        resolver.resolve(
            fontFamily = lato,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
            fontSynthesis = FontSynthesis.All
        ).value as Typeface
    }
    LaunchedEffect(scrollStateValue) {
        val currentMeasurement = (minMeasurement + selectedIndex).coerceIn(minMeasurement, maxMeasurement)
        onMeasurementChange(currentMeasurement)
    }
    LaunchedEffect(selectedIndex) {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
    }
    Box(
        modifier = modifier
            .height(150.dp)
            .onSizeChanged {
                pickerWidth = it.width
            },
        contentAlignment = Alignment.Center
    ) {

        Row(
            modifier = Modifier
                .fillMaxHeight()
                .horizontalScroll(scrollState)
                .padding(
                    horizontal = with(density) {
                        pickerWidth.toDp() / 2
                    }
                )
        ) {

            Canvas(
                modifier = Modifier
                    .width(
                        with(density) {
                            (totalTicks * tickSpacingPx).toDp()
                        }
                    )
                    .fillMaxHeight()
            ) {

                for (i in 0..totalTicks) {

                    val isSelected = i == selectedIndex
                    val isMajor = i % 5 == 0
                    val isEven = i % 2 == 0

                    val x = i * tickSpacingPx

                    val y = when {
                        isMajor -> size.height * 0.6f
                        isEven -> size.height * 0.5f
                        else -> size.height * 0.3f
                    }

                    val strokeWidth = if (isMajor) 8f else 5.5f

                    val tickLineColor = when {
                        isSelected -> selectedTickColor
                        isMajor -> majorTickColor
                        else -> tickColor
                    }

                    drawLine(
                        color = tickLineColor,
                        start = Offset(x = x, y = 0f),
                        end = Offset(x = x, y = y),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )

                    if (isMajor) {

                        drawContext.canvas.nativeCanvas.drawText(
                            "${minMeasurement + i}",
                            x,
                            size.height,
                            Paint().apply {

                                color =
                                    if (isSelected)
                                        selectedLabelColor.toArgb()
                                    else
                                        unSelectedLabelColor.toArgb()

                                textAlign = Paint.Align.CENTER

                                textSize =
                                    if (isSelected) 40f else 25f

                                isAntiAlias = true

                                typeface =
                                    if (isSelected)
                                        boldTypeface
                                    else
                                        regularTypeface
                            }
                        )
                    }
                }
            }
        }
        if (showOverlay) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.horizontalGradient(
                            0f to overlayColor,
                            0.15f to Color.Transparent,
                            0.85f to Color.Transparent,
                            1f to overlayColor
                        )
                    )
            )
        }
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_down),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-17).dp)
                .size(35.dp)
        )
    }
}