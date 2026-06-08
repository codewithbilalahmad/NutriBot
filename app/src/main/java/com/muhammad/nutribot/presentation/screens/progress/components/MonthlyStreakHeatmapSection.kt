package com.muhammad.nutribot.presentation.screens.progress.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.R
import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.utils.daysInMonth
import com.muhammad.nutribot.utils.firstDayOfMonth
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

@Composable
fun MonthlyStreakHeatmapSection(
    modifier: Modifier = Modifier,
    monthMeals: Map<LocalDate, List<Food>>,
    goalCalories: Int,
    cellSize: Dp = 30.dp,
    cellPadding: Dp = 4.dp,
    dayLabels: List<String> = listOf("M", "T", "W", "T", "F", "S", "S"),
) {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val firstDayOfMonth = today.firstDayOfMonth()
    val currentMonth =
        "${today.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${today.year}"
    val daysInMonth = today.daysInMonth()
    val startOffset = firstDayOfMonth.dayOfWeek.ordinal
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        border = BorderStroke(width = 1.5.dp, color = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_calories),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.error
                )
                Text(
                    text = stringResource(R.string.monthly_heatmap),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = currentMonth,
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.surface)
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(cellPadding)) {
                    dayLabels.forEachIndexed { index, day ->
                        val shape = when (index) {
                            0 -> RoundedCornerShape(
                                topStart = 100.dp,
                                topEnd = 100.dp,
                                bottomStart = 6.dp,
                                bottomEnd = 6.dp
                            )

                            dayLabels.lastIndex -> RoundedCornerShape(
                                topStart = 6.dp,
                                topEnd = 6.dp,
                                bottomStart = 100.dp,
                                bottomEnd = 100.dp
                            )

                            else -> RoundedCornerShape(6.dp)
                        }
                        Box(
                            modifier = Modifier
                                .size(cellSize)
                                .clip(shape)
                                .background(MaterialTheme.colorScheme.surfaceContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = day, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(cellPadding)
                ) {
                    val totalCells = daysInMonth + startOffset
                    val weeks = (totalCells + 6) / 7
                    for (week in 0 until weeks) {
                        Column(verticalArrangement = Arrangement.spacedBy(cellPadding)) {
                            for (dayOfWeek in 0..6) {
                                val index = week * 7 + dayOfWeek
                                val dayNumber = index - startOffset + 1
                                if (dayNumber in 1..daysInMonth) {
                                    val date =
                                        firstDayOfMonth.plus(DatePeriod(days = dayNumber - 1))
                                    val meals = monthMeals[date].orEmpty()
                                    val progress =
                                        if (meals.isEmpty()) 0f else (meals.sumOf { it.calories }
                                            .toFloat() / goalCalories).coerceIn(0f, 1f)
                                    val containerColor by animateColorAsState(
                                        targetValue = if (meals.isEmpty()) Color.Transparent else MaterialTheme.colorScheme.primary.copy(
                                            alpha = progress
                                        ),
                                        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                                        label = "containerColor"
                                    )
                                    val contentColor by animateColorAsState(
                                        targetValue = if (meals.isEmpty()) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onPrimary,
                                        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                                        label = "contentColor"
                                    )
                                    val borderColor by animateColorAsState(
                                        targetValue = if (meals.isEmpty()) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary.copy(
                                            alpha = progress
                                        ),
                                        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                                        label = "borderColor"
                                    )
                                    val shape =
                                        if (meals.isEmpty()) MaterialShapes.Square.toShape() else MaterialShapes.Circle.toShape()
                                    Box(
                                        modifier = Modifier
                                            .size(cellSize)
                                            .clip(shape)
                                            .border(
                                                width = 1.dp,
                                                color = borderColor,
                                                shape = shape
                                            )
                                            .background(containerColor),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = dayNumber.toString(),
                                            style = MaterialTheme.typography.bodyMedium.copy(color = contentColor)
                                        )
                                    }
                                } else {
                                    Spacer(modifier = Modifier.size(cellSize))
                                }
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                val colors = listOf(
                    MaterialTheme.colorScheme.primary.copy(0.3f),
                    MaterialTheme.colorScheme.primary.copy(0.5f),
                    MaterialTheme.colorScheme.primary.copy(0.7f),
                    MaterialTheme.colorScheme.primary
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.less),
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.surface)
                    )
                    Spacer(Modifier.width(6.dp))
                    colors.forEachIndexed { index, color ->
                        Box(
                            modifier = Modifier
                                .size(15.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(color)
                        )
                        if (index != colors.lastIndex) {
                            Spacer(Modifier.width(4.dp))
                        }
                    }
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.more),
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.surface)
                    )
                }
            }
        }
    }
}