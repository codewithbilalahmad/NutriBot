package com.muhammad.nutribot.presentation.screens.diary.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.R
import com.muhammad.nutribot.presentation.theme.CarbsColor
import com.muhammad.nutribot.presentation.theme.FatColor
import com.muhammad.nutribot.presentation.theme.ProteinColor
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CaloriesInTakeSection(
    modifier: Modifier = Modifier,
    weekCalenderPagerState: PagerState,
    eatenCalories: Int,
    goalCalories: Int,
    eatenFatGrams: Int,
    goalFatGrams: Int,
    eatenProteinGrams: Int,
    goalProteinGrams: Int,
    eatenCarbsGrams: Int,
    goalCarbsGrams: Int,
) {
    val scope = rememberCoroutineScope()
    val showMoveTodayOption by remember(weekCalenderPagerState) {
        derivedStateOf { weekCalenderPagerState.currentPage != weekCalenderPagerState.pageCount - 1 }
    }
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp)
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.calories_intake),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium
                )
            )
            AnimatedVisibility(
                visible = showMoveTodayOption,
                enter = slideInHorizontally(
                    initialOffsetX = { it }
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = {
                        it
                    }
                )) {
                Row(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                topStart = 24.dp,
                                bottomStart = 24.dp
                            )
                        )
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .clickable {
                            scope.launch {
                                weekCalenderPagerState.animateScrollToPage(weekCalenderPagerState.pageCount - 1)
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = stringResource(R.string.today),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
        CaloriesInTakeCard(
            eatenCalories = eatenCalories,
            goalCalories = goalCalories,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            eatenFatGrams = eatenFatGrams,
            goalFatGrams = goalFatGrams,
            eatenProteinGrams = eatenProteinGrams,
            goalProteinGrams = goalProteinGrams,
            eatenCarbsGrams = eatenCarbsGrams,
            goalCarbsGrams = goalCarbsGrams
        )
    }
}

@Composable
private fun CaloriesInTakeCard(
    modifier: Modifier = Modifier,
    eatenCalories: Int,
    goalCalories: Int,
    eatenFatGrams: Int,
    goalFatGrams: Int,
    eatenProteinGrams: Int,
    goalProteinGrams: Int,
    eatenCarbsGrams: Int,
    goalCarbsGrams: Int,
) {
    Card(
        modifier = modifier.dropShadow(
            shape = RoundedCornerShape(24.dp),
            shadow = Shadow(
                radius = 4.dp,
                spread = 4.dp,
                color = MaterialTheme.colorScheme.surfaceContainerLow
            )
        ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CaloriesStatistics(
                    label = R.string.eaten,
                    calories = eatenCalories
                )
                CaloriesLeftSection(
                    modifier = Modifier.size(100.dp),
                    goalCalories = goalCalories,
                    eatenCalories = eatenCalories,
                    content = {
                        CaloriesStatistics(
                            label = R.string.left,
                            calories = goalCalories - eatenCalories,
                            textStyle = MaterialTheme.typography.titleLarge
                        )
                    })
                CaloriesStatistics(
                    label = R.string.goal,
                    calories = goalCalories
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                NutritionStatistics(
                    label = R.string.protein,
                    color = ProteinColor,
                    modifier = Modifier.weight(1f),
                    icon = R.drawable.ic_protein,
                    eatenGram = eatenProteinGrams,
                    goalGram = goalProteinGrams
                )
                NutritionStatistics(
                    label = R.string.carbs,
                    color = CarbsColor,
                    modifier = Modifier.weight(1f),
                    icon = R.drawable.ic_carbs,
                    eatenGram = eatenCarbsGrams,
                    goalGram = goalCarbsGrams
                )
                NutritionStatistics(
                    label = R.string.fat,
                    color = FatColor,
                    modifier = Modifier.weight(1f),
                    icon = R.drawable.ic_fat,
                    eatenGram = eatenFatGrams,
                    goalGram = goalFatGrams
                )
            }
        }
    }
}


@Composable
private fun CaloriesLeftSection(
    modifier: Modifier,
    strokeWidth: Dp = 10.dp,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    dotColor: Color = MaterialTheme.colorScheme.onPrimary,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    eatenCalories: Int,
    goalCalories: Int, content: @Composable () -> Unit,
) {
    val progress = remember(eatenCalories, goalCalories) {
        (eatenCalories.toFloat() / goalCalories).coerceIn(0f, 1f)
    }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = MaterialTheme.motionScheme.slowEffectsSpec(),
        label = "animatedProgress"
    )
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidthPx = strokeWidth.toPx()
            val sweepAngle = 180f * animatedProgress
            drawArc(
                color = containerColor,
                startAngle = -180f,
                sweepAngle = 180f,
                useCenter = false,
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
            )
            drawArc(
                color = progressColor,
                startAngle = -180f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
            )
            val angleInDegree = -180f + sweepAngle
            val angleInRad = Math.toRadians(angleInDegree.toDouble())
            val radius = (size.minDimension / 2f) - (strokeWidthPx / 2)
            val centerX = size.width / 2
            val centerY = size.height / 2
            val x = centerX + radius * cos(angleInRad).toFloat()
            val y = centerY + radius * sin(angleInRad).toFloat()
            drawCircle(
                color = progressColor,
                radius = strokeWidthPx,
                center = Offset(x = x, y = y)
            )
            drawCircle(
                color = dotColor,
                radius = strokeWidthPx * 0.7f,
                center = Offset(x = x, y = y)
            )
        }
        content()
    }
}

@Composable
fun CaloriesStatistics(
    modifier: Modifier = Modifier,
    @StringRes label: Int,
    calories: Int,
    textStyle: TextStyle = MaterialTheme.typography.titleSmall,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = calories.toString(),
            style = textStyle.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.ExtraLight,
                color = MaterialTheme.colorScheme.surface
            )
        )
    }
}

@Composable
private fun NutritionStatistics(
    modifier: Modifier = Modifier,
    @StringRes label: Int,
    color: Color,
    icon: Int,
    goalGram: Int,
    eatenGram: Int,
) {
    val progress = remember(eatenGram, goalGram) {
        (eatenGram.toFloat() / goalGram).coerceIn(0f, 1f)
    }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = MaterialTheme.motionScheme.slowEffectsSpec(),
        label = "animatedProgress"
    )
    val progressAnnotatedString = buildAnnotatedString {
        withStyle(
            SpanStyle(
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(eatenGram.toString())
        }
        withStyle(
            SpanStyle(
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                color = MaterialTheme.colorScheme.surface,
                fontWeight = FontWeight.ExtraLight
            )
        ) {
            append("/${goalGram}g")
        }
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = stringResource(label),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.surface,
                    fontWeight = FontWeight.ExtraLight
                )
            )
        }
        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(color)
            )
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(text = progressAnnotatedString)
    }
}
