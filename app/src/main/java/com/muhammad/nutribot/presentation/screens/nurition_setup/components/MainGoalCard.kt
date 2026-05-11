package com.muhammad.nutribot.presentation.screens.nurition_setup.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammad.nutribot.domain.model.MainGoal
import com.muhammad.nutribot.utils.rippleClickable

@Composable
fun MainGoalCard(
    modifier: Modifier = Modifier,
    mainGoal: MainGoal,
    isSelected: Boolean,
    onSelectMainGoal: (MainGoal) -> Unit,
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
        label = "borderColor"
    )
    val borderWidth by animateDpAsState(
        targetValue = if (isSelected) 2.dp else 1.dp,
        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
        label = "borderColor"
    )
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.background,
        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
        label = "containerColor"
    )
    Card(
        modifier = modifier.rippleClickable {
            onSelectMainGoal(mainGoal)
        },
        border = BorderStroke(width = borderWidth, color = borderColor),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = mainGoal.icon, fontSize = 25.sp)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)){
                    Text(
                        text = stringResource(mainGoal.label),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = stringResource(mainGoal.desp),
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.surface)
                    )
                }
            }
        }
    }
}