package com.muhammad.nutribot.presentation.screens.diary.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.muhammad.nutribot.presentation.components.calender.WeekCalender
import kotlinx.datetime.LocalDate

@Composable
fun DiaryTopbar(
    modifier: Modifier = Modifier,
    streak: Int,
    foods: List<Food>,
    selectedDate: LocalDate,
    weekCalenderPagerState: PagerState,
    onDateSelected: (LocalDate) -> Unit,
    onSettingClick: () -> Unit, weekRange: IntRange,
) {
    Column(modifier = modifier.statusBarsPadding()) {
        WeekCalender(
            selectedDate = selectedDate,
            weekRange = weekRange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            foodList = foods,
            pagerState = weekCalenderPagerState,
            onDateSelected = onDateSelected,
            trailingContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .dropShadow(
                                shape = CircleShape, shadow = Shadow(
                                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                                    spread = 2.dp,
                                    radius = 2.dp
                                )
                            )
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.background)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            6.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_streak),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
                        )
                        Text(
                            text = "$streak",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    IconButton(onClick = onSettingClick, shapes = IconButtonDefaults.shapes()) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_setting),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                            tint = MaterialTheme.colorScheme.surface
                        )
                    }
                }
            }
        )
    }
}