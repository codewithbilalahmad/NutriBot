package com.muhammad.nutribot.presentation.screens.nurition_setup.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import com.muhammad.nutribot.R
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammad.nutribot.domain.model.ActivityLevel
import com.muhammad.nutribot.utils.rippleClickable

@Composable
fun ActivityLevelCard(
    modifier: Modifier = Modifier,
    activityLevel: ActivityLevel,
    isSelected: Boolean,
    onSelectActivityLevel: (ActivityLevel) -> Unit,
) {
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surfaceContainer,
        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
        label = "containerColor"
    )
    Card(
        modifier = modifier.rippleClickable(onClick = {
            onSelectActivityLevel(activityLevel)
        }),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (isSelected) Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    ) else Modifier
                )
                .padding(horizontal = 12.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = activityLevel.icon, fontSize = 25.sp)
                Text(
                    text = stringResource(activityLevel.label),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn(MaterialTheme.motionScheme.slowEffectsSpec()),
                exit = fadeOut(MaterialTheme.motionScheme.slowEffectsSpec()),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_check),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}