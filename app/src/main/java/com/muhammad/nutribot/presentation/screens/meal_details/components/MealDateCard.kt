package com.muhammad.nutribot.presentation.screens.meal_details.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.R
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import kotlin.time.Clock

@Composable
fun MealDateCard(modifier: Modifier = Modifier, selectedDate: LocalDate, onClick: () -> Unit) {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val dateLabel = when (selectedDate) {
        today -> "${stringResource(R.string.today)}, ${selectedDate.day} ${
            selectedDate.month.name.lowercase().replaceFirstChar { it.uppercase() }
        }"
        today.minus(DatePeriod(days = 1)) -> {
            "${stringResource(R.string.yesterday)}, ${selectedDate.day} ${
                selectedDate.month.name.lowercase().replaceFirstChar { it.uppercase() }
            }"
        }
        else -> "${selectedDate.day} ${selectedDate.month.name.lowercase().replaceFirstChar { it.uppercase() }}"
    }
    Card(
        modifier = modifier, onClick = onClick,
        shape = CircleShape,
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.surfaceVariant),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = dateLabel, style = MaterialTheme.typography.bodyMedium
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_date),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}