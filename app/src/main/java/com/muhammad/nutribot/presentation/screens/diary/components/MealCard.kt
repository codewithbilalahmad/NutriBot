package com.muhammad.nutribot.presentation.screens.diary.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.muhammad.nutribot.R
import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.presentation.components.image.AppImage
import com.muhammad.nutribot.presentation.components.image.ImagePlaceholder
import com.muhammad.nutribot.presentation.screens.meal_details.components.IngredientItem
import com.muhammad.nutribot.presentation.theme.CarbsColor
import com.muhammad.nutribot.presentation.theme.FatColor
import com.muhammad.nutribot.presentation.theme.ProteinColor
import com.muhammad.nutribot.utils.isNetworkUrl
import com.muhammad.nutribot.utils.loadingEffect
import com.muhammad.nutribot.utils.rippleClickable
import com.muhammad.nutribot.utils.toFormattedTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

@Composable
fun MealCard(
    modifier: Modifier = Modifier,
    meal: Food,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onMealClick: (Food) -> Unit,
    onDeleteMeal: (Food) -> Unit,
) {
    val dateTime = remember(meal.eatenAt) {
        Instant.fromEpochMilliseconds(meal.eatenAt).toLocalDateTime(TimeZone.currentSystemDefault())
    }
    Card(
        modifier = modifier.rippleClickable{
            onMealClick(meal)
        }.dropShadow(shape = RoundedCornerShape(16.dp), shadow = Shadow(
            radius = 2.dp,
            spread = 2.dp,
            color = MaterialTheme.colorScheme.surfaceContainerLow
        )),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.CenterStart){
                if(meal.mealImageUrl.isNotEmpty()){
                    with(sharedTransitionScope){
                        if(meal.mealImageUrl.isNetworkUrl()){
                            AsyncImage(model = meal.mealImageUrl, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surfaceContainer)
                                .loadingEffect()
                                .sharedElement(
                                    sharedContentState = rememberSharedContentState(
                                        key = "meal_image"
                                    ),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    boundsTransform = { _, _ ->
                                        tween(durationMillis = 300, easing = LinearEasing)
                                    }
                                ))
                        } else{
                            AppImage(
                                image = meal.mealImageUrl,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .sharedElement(
                                        sharedContentState = rememberSharedContentState(
                                            key = "meal_image"
                                        ),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                        boundsTransform = { _, _ ->
                                            tween(durationMillis = 300, easing = LinearEasing)
                                        }
                                    )
                            )
                        }
                    }
                } else{
                    ImagePlaceholder(size= 60.dp, borderWidth = 2.5.dp)
                }
            }
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
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2, overflow = TextOverflow.Ellipsis
                        )
                    }
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_delete),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .size(28.dp)
                            .rippleClickable {
                                onDeleteMeal(meal)
                            })
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
}