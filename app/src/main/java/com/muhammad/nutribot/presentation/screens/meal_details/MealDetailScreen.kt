package com.muhammad.nutribot.presentation.screens.meal_details

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Spring.DampingRatioMediumBouncy
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.muhammad.nutribot.R
import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.presentation.components.button.PrimaryButton
import com.muhammad.nutribot.presentation.components.image.AppImage
import com.muhammad.nutribot.presentation.screens.meal_details.components.MealDateCard
import com.muhammad.nutribot.presentation.screens.meal_details.components.MealIngredientCard
import com.muhammad.nutribot.presentation.screens.meal_details.components.MealNutritionSection
import com.muhammad.nutribot.presentation.screens.meal_details.components.NumberOfServingsSection
import com.muhammad.nutribot.presentation.screens.meal_details.components.TotalCaloriesCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun MealDetailScreen(
    navHostController: NavHostController,
    viewModel: MealDetailViewModel = koinViewModel(),
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    food: Food,
) {
    val density= LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val listState = rememberLazyListState()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollOffset by remember {
        derivedStateOf {
            listState.firstVisibleItemScrollOffset
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(), bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                PrimaryButton(
                    text = "${stringResource(R.string.log_to)} ${state.selectedDate.day} ${
                        state.selectedDate.month.name.lowercase()
                            .replaceFirstChar { it.uppercase() }
                    }",
                    onClick = {},
                    contentPadding = PaddingValues(vertical = 16.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }, containerColor = MaterialTheme.colorScheme.background
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
            ) {
                with(sharedTransitionScope) {
                    AppImage(
                        image = food.mealImageUrl,
                        modifier = Modifier
                            .graphicsLayer{
                                translationY = with(density){
                                    -scrollOffset.toFloat() * 0.5f
                                }
                            }
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
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 278.dp, bottom = 100.dp),
                state = listState
            ) {
                item("fav_icon") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem()
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
                                    topEnd = 16.dp
                                )
                            )
                            .background(MaterialTheme.colorScheme.background)
                            .animateItem()
                            .padding(top = 8.dp)
                    ) {
                        Text(
                            text = food.name,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .padding(horizontal = 4.dp)
                                .clickable {}
                                .padding(
                                    horizontal = 12.dp,
                                    vertical = 6.dp
                                )
                        )

                        Spacer(Modifier.height(12.dp))

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
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.ingredients_found),
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(Modifier.height(12.dp))
                        food.ingredients.forEach { ingredient ->
                            MealIngredientCard(
                                ingredient = ingredient,
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                onClick = {

                                })
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .graphicsLayer{
                        translationY = -scrollOffset * 0.5f
                    }
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .height(100.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.background.copy(0.7f),
                                MaterialTheme.colorScheme.background.copy(0.6f),
                                MaterialTheme.colorScheme.background.copy(0.5f),
                                MaterialTheme.colorScheme.background.copy(0.4f),
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
            MealDateCard(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                selectedDate = state.selectedDate, onClick = {}
            )
        }
    }
}