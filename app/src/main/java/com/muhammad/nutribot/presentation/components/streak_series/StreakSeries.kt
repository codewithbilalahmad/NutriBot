package com.muhammad.nutribot.presentation.components.streak_series

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.R
import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.presentation.theme.FlameYellow
import com.muhammad.nutribot.utils.getCurrentWeekDates
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
fun StreakSeries(modifier: Modifier = Modifier, weekMeals: List<Food>, showShadow : Boolean = true) {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val weekDates = remember(today) {
        getCurrentWeekDates()
    }
    val mealByDate = remember(weekMeals) {
        weekMeals.groupBy {
            val eatenAt = Instant.fromEpochMilliseconds(it.eatenAt)
            eatenAt.toLocalDateTime(TimeZone.currentSystemDefault()).date
        }
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        weekDates.forEach { date ->
            val isTodayDate = date == today
            val hasMeal = mealByDate[date]?.firstOrNull() != null
            val borderColor by animateColorAsState(
                targetValue = when {
                    isTodayDate -> MaterialTheme.colorScheme.primary
                    else -> Color.Transparent
                },
                animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                label = "borderColor"
            )
            val streakColor by animateColorAsState(
                targetValue = if (hasMeal) FlameYellow else MaterialTheme.colorScheme.surface,
                animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                label = "streakColor"
            )
            val contentColor by animateColorAsState(
                targetValue = if (isTodayDate) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.surface,
                animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                label = "streakColor"
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .then(
                        when{
                            isTodayDate -> Modifier
                            showShadow -> Modifier.dropShadow(
                                shape = RoundedCornerShape(16.dp), shadow = Shadow(
                                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                                    spread = 2.dp,
                                    radius = 2.dp
                                )
                            )
                            else -> Modifier
                        }
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = 2.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_calories),
                    contentDescription = null, modifier = Modifier.size(22.dp), tint = streakColor
                )
                Text(
                    text = date.dayOfWeek.name.take(3).lowercase().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = contentColor,
                        fontWeight = FontWeight.Light
                    )
                )
            }
        }
    }
}