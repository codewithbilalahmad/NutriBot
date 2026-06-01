package com.muhammad.nutribot.presentation.screens.search_meal

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.muhammad.nutribot.R
import com.muhammad.nutribot.data.local.mappers.toFood
import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.presentation.components.textfield.AppTextField
import com.muhammad.nutribot.presentation.navigation.Destination
import com.muhammad.nutribot.presentation.screens.search_meal.components.HistoryMealCard
import com.muhammad.nutribot.presentation.screens.search_meal.components.SearchMealCard
import com.muhammad.nutribot.presentation.screens.search_meal.components.SearchMealLoader
import com.muhammad.nutribot.utils.ObserveAsEvents
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchMealScreen(
    navHostController: NavHostController,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: SearchMealViewModel = koinViewModel(),
) {
    val layoutDirection = LocalLayoutDirection.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is SearchMealEvent.OnSearchMealAdded -> {
                navHostController.navigate(Destination.MealDetailScreen(event.meal))
            }
        }
    }
    @OptIn(FlowPreview::class)
    LaunchedEffect(Unit) {
        snapshotFlow { state.query.text.toString() }.debounce(500).distinctUntilChanged()
            .collectLatest { query ->
                viewModel.onAction(SearchMealAction.OnSearchFoods(query))
            }
    }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        navHostController.navigateUp()
                    }, shapes = IconButtonDefaults.shapes()) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left),
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(text = stringResource(R.string.search_meal))
                },
                actions = {
                    IconButton(onClick = {
                        navHostController.navigate(
                            Destination.MealDetailScreen(
                                Food(
                                    id = 0L,
                                    name = "My Dish",
                                    calories = 300,
                                    protein = 50,
                                    fat = 20,
                                    carbs = 30,
                                    numberOfServings = 1,
                                    confidenceScore = 100,
                                    eatenAt = 0L,
                                    isFavorite = false,
                                    ingredients = emptyList(),
                                    servingSize = ""
                                )
                            )
                        )
                    }, shapes = IconButtonDefaults.shapes()) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_edit),
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
            AppTextField(
                state = state.query,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                shape = CircleShape,
                leadingIcon = R.drawable.ic_search,
                contentPadding = PaddingValues(12.dp),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
                hint = R.string.search_meal
            )
            Spacer(Modifier.height(8.dp))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceContainer,
                thickness = 1.dp
            )
        }
    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding() + 8.dp,
                bottom = paddingValues.calculateBottomPadding() + 50.dp,
                start = paddingValues.calculateLeftPadding(layoutDirection),
                end = paddingValues.calculateRightPadding(layoutDirection)
            )
        ) {
            if (state.query.text.isEmpty()) {
                item("recent_meals_title") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, end = 12.dp, top = 16.dp)
                            .animateItem(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.recent_meals),
                            style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.surface)
                        )
                    }
                }
                when{
                    state.isLoadingHistoryFoods ->{
                        items(10, key = { it }, contentType = {
                            "loading_history_${it}"
                        }){
                            SearchMealLoader(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp, start = 16.dp, end = 16.dp)
                                    .animateItem()
                            )
                        }
                    }
                    else ->{
                        items(state.historyFoods, key = { it.id }, contentType = {
                            "FoodHistory_${it.id}"
                        }) { historyFood ->
                            HistoryMealCard(
                                meal = historyFood,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp, start = 16.dp, end = 16.dp)
                                    .animateItem(),
                                onMealClick = {
                                    navHostController.navigate(Destination.MealDetailScreen(historyFood.toFood()))
                                },
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                        }
                    }
                }
            } else {
                when {
                    state.isLoadingSearchedFood -> {
                        items(10, key = { it }, contentType = {
                            "loading_${it}"
                        }) {
                            SearchMealLoader(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp, start = 16.dp, end = 16.dp)
                                    .animateItem()
                            )
                        }
                    }

                    state.searchFoodsError != null && !state.isLoadingSearchedFood -> {
                        item("search_error") {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItem()
                                    .padding(vertical = 50.dp, horizontal = 24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.meal),
                                    contentDescription = null,
                                    modifier = Modifier.size(180.dp)
                                )
                                Spacer(Modifier.height(12.dp))
                                Text(
                                    text = stringResource(R.string.no_meal_found),
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = stringResource(R.string.no_meal_found_desp),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.surface,
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                        }
                    }

                    else -> {
                        items(state.searchedFood, key = { it.id }, contentType = {
                            "searchMeal_${it.id}"
                        }) { meal ->
                            SearchMealCard(
                                meal = meal,
                                onMealClick = { meal ->
                                    viewModel.onAction(SearchMealAction.OnSearchMealClick(meal))
                                },
                                animatedVisibilityScope = animatedVisibilityScope,
                                sharedTransitionScope = sharedTransitionScope,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp, start = 16.dp, end = 16.dp)
                                    .animateItem()
                            )
                        }
                    }
                }
            }
        }
    }
}