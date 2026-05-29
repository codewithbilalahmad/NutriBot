package com.muhammad.nutribot.presentation.screens.favourite_meals.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.R
import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.presentation.components.image.AppImage
import com.muhammad.nutribot.presentation.screens.meal_details.components.IngredientItem
import com.muhammad.nutribot.presentation.theme.CarbsColor
import com.muhammad.nutribot.presentation.theme.FatColor
import com.muhammad.nutribot.presentation.theme.ProteinColor
import com.muhammad.nutribot.utils.rippleClickable
import com.muhammad.nutribot.utils.toFormattedTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

@Composable
fun FavouriteMealCard(
    modifier: Modifier = Modifier,
    meal: Food,
    onClick: (Food) -> Unit,
    onUnFavouriteMeal: (Long) -> Unit
) {
    val dateTime = remember(meal.eatenAt) {
        Instant.fromEpochMilliseconds(meal.eatenAt).toLocalDateTime(TimeZone.currentSystemDefault())
    }
    Box(modifier = modifier) {
        Card(
            modifier = modifier,
            onClick = {
                onClick(meal)
            },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppImage(
                    image = meal.mealImageUrl,
                    modifier = Modifier
                        .size(width = 110.dp, height = 130.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 16.dp,
                                bottomStart = 16.dp
                            )
                        )
                )
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceContainer)
                                    .padding(horizontal = 6.dp, vertical = 2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dateTime.time.toFormattedTime(),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Text(
                                text = meal.name,
                                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.surface),
                                maxLines = 2, overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_calories),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "${meal.calories}  ${stringResource(R.string.kcal)}",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IngredientItem(
                            icon = R.drawable.ic_protein,
                            label = "${meal.protein} ${stringResource(R.string.grams)}",
                            color = ProteinColor
                        )
                        IngredientItem(
                            icon = R.drawable.ic_fat,
                            label = "${meal.fat} ${stringResource(R.string.grams)}",
                            color = FatColor
                        )
                        IngredientItem(
                            icon = R.drawable.ic_carbs,
                            label = "${meal.carbs} ${stringResource(R.string.grams)}",
                            color = CarbsColor
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 8.dp, y = (-8).dp)
                .size(28.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = CircleShape
                )
                .background(color = MaterialTheme.colorScheme.background).rippleClickable{
                    onUnFavouriteMeal(meal.id)
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_delete),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}