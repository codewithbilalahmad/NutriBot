package com.muhammad.nutribot.presentation.screens.meal_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.R
import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.utils.rippleClickable

@Composable
fun NumberOfServingsSection(
    modifier: Modifier = Modifier,
    food: Food,
    onAddServings: () -> Unit,
    onMinusServings: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.number_of_servings),
            style = MaterialTheme.typography.bodyLarge
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .dropShadow(
                        shape = CircleShape, shadow = Shadow(
                            radius = 2.dp,
                            spread = 2.dp,
                            color = MaterialTheme.colorScheme.surfaceContainerLow
                        )
                    )
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background)
                    .rippleClickable{
                        onMinusServings()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_minus),
                    contentDescription = null,
                    modifier = Modifier.size(22.dp), tint = MaterialTheme.colorScheme.surface
                )
            }
            Text(
                text = food.numberOfServings.toString(),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .dropShadow(
                        shape = CircleShape, shadow = Shadow(
                            radius = 2.dp,
                            spread = 2.dp,
                            color = MaterialTheme.colorScheme.surfaceContainerLow
                        )
                    )
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background)
                    .rippleClickable{
                        onAddServings()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_add),
                    contentDescription = null,
                    modifier = Modifier.size(22.dp), tint = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}