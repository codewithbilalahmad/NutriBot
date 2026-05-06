package com.muhammad.nutribot.presentation.screens.nurition_setup.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.R
import com.muhammad.nutribot.domain.model.NuritionSetupStep

@Composable
fun NuritionSetupTopbar(
    modifier: Modifier = Modifier,
    currentStepIndex: Int,
    totalSteps: Int,
    onPreviousStepClick : () -> Unit,
    currentStep: NuritionSetupStep,
) {
    Column(modifier = modifier.statusBarsPadding(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPreviousStepClick, shapes = IconButtonDefaults.shapes()) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left),
                    contentDescription = null
                )
            }
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(10.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                val progress = (currentStepIndex + 1).toFloat() / totalSteps.toFloat()
                val animatedProgress by animateFloatAsState(
                    targetValue = progress,
                    animationSpec = MaterialTheme.motionScheme.slowEffectsSpec(),
                    label = "animatedProgress"
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(
                            MaterialTheme.colorScheme.primary
                        )
                )
            }
            Spacer(Modifier.width(16.dp))
            Text(text = "${currentStepIndex + 1} / $totalSteps", style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(Modifier.height(12.dp))
        AnimatedContent(targetState = currentStep, transitionSpec = {
            fadeIn() togetherWith fadeOut()
        }) {step ->
            Text(
                text = stringResource(step.label),
                maxLines = 2,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center)
            )
        }
    }
}