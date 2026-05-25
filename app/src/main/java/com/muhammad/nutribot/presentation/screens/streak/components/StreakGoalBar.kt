package com.muhammad.nutribot.presentation.screens.streak.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import com.muhammad.nutribot.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.domain.model.StreakGoalStep

@Composable
fun StreakGoalBar(
    modifier: Modifier = Modifier, streak: Int,
    strokeHeight: Dp = 10.dp,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    steps: List<StreakGoalStep> = listOf(
        StreakGoalStep(step = 1, shape = CircleShape),
        StreakGoalStep(step = 3, shape = MaterialShapes.Cookie7Sided.toShape()),
        StreakGoalStep(step = 5, shape = MaterialShapes.Cookie9Sided.toShape()),
        StreakGoalStep(step = 10, shape = MaterialShapes.Cookie12Sided.toShape()),
    ),
) {
    val maxStep = steps.last().step.toFloat()
    BoxWithConstraints(modifier = modifier) {
        val totalWidth = maxWidth
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(strokeHeight)
                .align(Alignment.CenterStart)
                .clip(CircleShape)
                .background(inactiveColor)
        ) {
            val progress = (streak / maxStep).coerceIn(0f, 1f)
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(activeColor)
            )
        }
        steps.forEachIndexed { index, step ->
            val isLastStep = index == steps.size - 1
            val fraction = step.step / maxStep
            val isReached = streak >= step.step
            val containerColor by animateColorAsState(
                targetValue = if (isReached) activeColor else MaterialTheme.colorScheme.surfaceVariant,
                animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                label = "color"
            )
            val tint by animateColorAsState(
                targetValue = if (isReached) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.surface,
                animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                label = "color"
            )
            val contentColor by animateColorAsState(
                targetValue = if (isReached) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                label = "color"
            )
            val size by animateDpAsState(
                targetValue = if (isReached) 32.dp else 28.dp,
                animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                label = "size"
            )
            val iconSize by animateDpAsState(
                targetValue = if (isReached) 20.dp else 16.dp,
                animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                label = "size"
            )
            Column(
                modifier = Modifier.offset(x = (totalWidth * fraction - size / 2) - (if (isLastStep) 16.dp else 0.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(size)
                        .clip(step.shape)
                        .background(containerColor), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_date),
                        contentDescription = null,
                        modifier = Modifier.size(iconSize), tint = tint
                    )
                }
                Text(
                    text = step.step.toString(),
                    style = if (isReached) MaterialTheme.typography.bodyLarge.copy(
                        color = contentColor,
                        fontWeight = FontWeight.Bold
                    ) else MaterialTheme.typography.labelLarge.copy(color = contentColor)
                )
            }
        }
    }
}