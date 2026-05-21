package com.muhammad.nutribot.presentation.screens.meal_details

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Spring.DampingRatioMediumBouncy
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.muhammad.nutribot.R
import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.presentation.components.image.AppImage
import com.muhammad.nutribot.presentation.screens.meal_details.components.MealNutritionSection
import com.muhammad.nutribot.presentation.screens.meal_details.components.NumberOfServingsSection
import com.muhammad.nutribot.presentation.screens.meal_details.components.TotalCaloriesCard
import kotlin.math.min

@Composable
fun MealDetailScreen(
    navHostController: NavHostController,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    food: Food,
) {

    val layoutDirection = LocalLayoutDirection.current
    val listState = rememberLazyListState()

    val scrollOffset = remember {
        derivedStateOf {
            if (listState.firstVisibleItemIndex == 0) {
                listState.firstVisibleItemScrollOffset.toFloat()
            } else {
                1000f
            }
        }
    }

    val imageTranslateY = -(scrollOffset.value * 0.5f)

    val overlayAlpha = min(1f, scrollOffset.value / 400f)

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    PaddingValues(
                        start = paddingValues.calculateStartPadding(layoutDirection),
                        end = paddingValues.calculateEndPadding(layoutDirection),
                        bottom = paddingValues.calculateBottomPadding()
                    )
                )
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .graphicsLayer {
                        translationY = imageTranslateY
                    }
            ) {

                with(sharedTransitionScope) {
                    AppImage(
                        image = food.mealImageUrl,
                        modifier = Modifier
                            .fillMaxSize()
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(
                                    key = "meal_image"
                                ),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = { _, _ ->
                                    spring(
                                        dampingRatio = DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    )
                                }
                            )
                    )
                }

                // DARK OVERLAY
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = overlayAlpha * 0.7f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.2f)
                                )
                            )
                        )
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 278.dp, bottom = 2000.dp),
                state = listState
            ) {
                item("fav_icon") {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        IconButton(
                            onClick = {},
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.Black.copy(alpha = 0.3f)
                            )
                        ) {

                            val icon =
                                if (food.isFavorite)
                                    R.drawable.ic_favourite_filled
                                else
                                    R.drawable.ic_favourite_outlined

                            val tint =
                                if (food.isFavorite)
                                    MaterialTheme.colorScheme.error
                                else
                                    MaterialTheme.colorScheme.surfaceVariant

                            Icon(
                                imageVector = ImageVector.vectorResource(icon),
                                contentDescription = null,
                                tint = tint
                            )
                        }
                    }
                }
                item("food_details") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 24.dp
                                )
                            )
                            .background(MaterialTheme.colorScheme.background)
                            .padding(top = 4.dp)
                    ) {
                        Text(
                            text = food.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {}
                                .padding(
                                    horizontal = 12.dp,
                                    vertical = 6.dp
                                )
                        )

                        Spacer(Modifier.height(16.dp))

                        NumberOfServingsSection(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            food = food,
                            onAddServings = {},
                            onMinusServings = {}
                        )

                        Spacer(Modifier.height(16.dp))

                        TotalCaloriesCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            food = food,
                            onClick = {}
                        )

                        Spacer(Modifier.height(16.dp))

                        MealNutritionSection(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            food = food,
                            onFatClick = {},
                            onCarbsClick = {},
                            onProteinClick = {}
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.background.copy(
                                    alpha = overlayAlpha
                                ),
                                Color.Transparent
                            )
                        )
                    )
            )

            IconButton(
                onClick = {
                    navHostController.navigateUp()
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Black.copy(alpha = 0.3f),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {

                Icon(
                    imageVector = ImageVector.vectorResource(
                        R.drawable.ic_arrow_left
                    ),
                    contentDescription = null
                )
            }
        }
    }
}