package com.muhammad.nutribot.presentation.screens.welcome.components

import androidx.annotation.StringRes
import com.muhammad.nutribot.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeNutritionSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                translationY = -100.dp.toPx()
            }) {
        NutritionItem(
            modifier = Modifier
                .weight(1f)
                .height(250.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.3f),
                    shape = CircleShape
                )
                .background(
                    MaterialTheme.colorScheme.primaryContainer
                ).padding(12.dp), label = R.string.calories, icon = R.drawable.ic_calories
        )
        NutritionItem(
            modifier = Modifier
                .weight(1f)
                .height(290.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.3f),
                    shape = CircleShape
                )
                .background(
                    MaterialTheme.colorScheme.primaryContainer
                ).padding(12.dp), label = R.string.protein,icon = R.drawable.ic_protein
        )
        NutritionItem(
            modifier = Modifier
                .weight(1f)
                .height(330.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.3f),
                    shape = CircleShape
                )
                .background(
                    MaterialTheme.colorScheme.primaryContainer
                ).padding(12.dp), label = R.string.fat,icon = R.drawable.ic_fat
        )
    }
}

@Composable
private fun NutritionItem(modifier: Modifier = Modifier, @StringRes label: Int, icon: Int) {
    Box(modifier = modifier, contentAlignment = Alignment.BottomCenter) {
        Column(
            modifier = Modifier
                .aspectRatio(1f)
                .clip(CircleShape)
                .border(
                    width = 1.5.dp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.5f),
                    shape = CircleShape
                )
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = null,
                modifier = Modifier.size(35.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(text = stringResource(label), style = MaterialTheme.typography.bodyLarge)
        }
    }
}
