package com.muhammad.nutribot.presentation.components.calender

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.R
import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.utils.getWeekDatesFrom
import com.muhammad.nutribot.utils.rippleClickable
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

@Composable
fun WeekCalender(
    modifier: Modifier = Modifier,
    foodList: List<Food> = emptyList(),
    weekRange: IntRange,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    trailingContent: @Composable () -> Unit,
    pagerState: PagerState,
    initialDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val foodByDate = remember(foodList) {
        foodList.groupBy { it.eatenAt.toLocalDateTime(TimeZone.currentSystemDefault()).date }
    }
    val isToday = selectedDate == today
    val isSameYear = selectedDate.year == today.year
    val isYesterday = selectedDate == today.minus(1, DateTimeUnit.DAY)
    val month = selectedDate.month.name.lowercase().replaceFirstChar { it.uppercase() }
    val day = selectedDate.day
    val year = selectedDate.year
    val dateLabel = when {
        isToday -> "${stringResource(R.string.today)}, $day $month"
        isYesterday -> "${stringResource(R.string.yesterday)}, $day $month"
        !isSameYear -> "$day $month, $year"
        else -> "$day $month"
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = dateLabel, style = MaterialTheme.typography.titleLarge)
            trailingContent()
        }
        HorizontalPager(
            state = pagerState, beyondViewportPageCount = 1,
            modifier = Modifier.fillMaxWidth(), key = { it }
        ) { page ->
            val weekOffset = weekRange.first + page
            val weekStart = initialDate.plus(weekOffset, DateTimeUnit.WEEK)
            val dates = getWeekDatesFrom(weekStart)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                dates.forEach { date ->
                    val isSelected = date == selectedDate
                    val hasFood = foodByDate[date]?.firstOrNull() != null
                    val containerColor by animateColorAsState(
                        targetValue = when {
                            isSelected -> MaterialTheme.colorScheme.surfaceContainer
                            else -> MaterialTheme.colorScheme.background
                        },
                        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                        label = "containerColor"
                    )
                    val borderColor by animateColorAsState(
                        targetValue = when {
                            isSelected -> MaterialTheme.colorScheme.primary
                            else -> Color.Transparent
                        },
                        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                        label = "borderColor"
                    )
                    Column(
                        modifier = Modifier
                            .width(40.dp)
                            .rippleClickable{
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                                onDateSelected(date)
                            }
                            .then(if(isSelected) Modifier else Modifier.dropShadow(
                                shape = RoundedCornerShape(16.dp), shadow = Shadow(
                                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                                    spread = 2.dp,
                                    radius = 2.dp
                                )
                            ))
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                width = 2.dp,
                                color = borderColor,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .background(containerColor)
                            .padding(vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        AnimatedContent(targetState = date.dayOfWeek, transitionSpec = {
                            fadeIn() togetherWith fadeOut()
                        }) {dayOfWeek ->
                            Text(
                                text = dayOfWeek.name.take(3).lowercase()
                                    .replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light)
                            )
                        }
                        Text(
                            text = date.day.toString(),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Box(
                            modifier = Modifier
                                .graphicsLayer{
                                    alpha = if(hasFood) 1f else 0f
                                }
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(
                                    MaterialTheme.colorScheme.primary
                                )
                        )
                    }
                }
            }
        }
    }
}