package com.muhammad.nutribot.presentation.screens.diary

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.muhammad.nutribot.R
import com.muhammad.nutribot.presentation.components.alert_dialog.AppAlertDialog
import com.muhammad.nutribot.presentation.components.bottom_sheet.AppBottomSheet
import com.muhammad.nutribot.presentation.navigation.BottomNavigationBar
import com.muhammad.nutribot.presentation.navigation.Destination
import com.muhammad.nutribot.presentation.screens.diary.components.AddFoodOptionCard
import com.muhammad.nutribot.presentation.screens.diary.components.CaloriesInTakeSection
import com.muhammad.nutribot.presentation.screens.diary.components.DiaryTopbar
import com.muhammad.nutribot.presentation.screens.diary.components.EmptyFoodSection
import com.muhammad.nutribot.presentation.screens.diary.components.MealCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun DiaryScreen(
    navHostController: NavHostController,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: DiaryViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val weekRange = state.weekRange
    val listState = rememberLazyListState()
    val scrollOffset by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex * 1000 +
                    listState.firstVisibleItemScrollOffset
        }
    }
    val weekCalenderPagerState = rememberPagerState(initialPage = weekRange.count() - 1) { weekRange.count() }
    val galleryPicker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {uri ->
        if(uri != null){
            navHostController.navigate(Destination.ScanMealScreen(galleryUri = uri.toString()))
        }
    }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        DiaryTopbar(
            modifier = Modifier.fillMaxWidth(),
            onSettingClick = {
                navHostController.navigate(Destination.SettingScreen)
            }, onStreakClick = {
                navHostController.navigate(Destination.StreakScreen)
            },
            onDateSelected = { date ->
                viewModel.onAction(DiaryAction.OnDateSelected(date))
            },
            foods = state.allFoods,
            streak = state.streak,
            weekRange = weekRange,
            weekCalenderPagerState = weekCalenderPagerState,
            selectedDate = state.selectedDate,
        )
    }, bottomBar = {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            AnimatedVisibility(
                visible = state.foods.isEmpty() && !state.isLoadingFoods,
                enter = fadeIn(MaterialTheme.motionScheme.fastEffectsSpec()),
                exit = fadeOut(MaterialTheme.motionScheme.fastEffectsSpec()),
            ) {
                EmptyFoodSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 50.dp)
                )
            }
            BottomNavigationBar(navHostController = navHostController, onAddFoodClick = {
                viewModel.onAction(DiaryAction.OnToggleAddFoodBottomSheet)
            })
        }
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_doughnut),
                contentDescription = null,
                modifier = Modifier
                    .graphicsLayer {
                        translationX = maxOf(-150.dp.toPx(),-25.dp.toPx() - (scrollOffset.toFloat() * 0.5f))
                        translationY = maxOf(-150.dp.toPx(),-25.dp.toPx() - (scrollOffset.toFloat() * 0.5f))
                    }
                    .size(150.dp)
                    .align(Alignment.TopStart),
                tint = Color.Unspecified
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_candy),
                contentDescription = null,
                modifier = Modifier
                    .graphicsLayer {
                        translationX = minOf(
                            150.dp.toPx(),
                            25.dp.toPx() + (scrollOffset * 0.5f)
                        )

                        translationY = maxOf(
                            -150.dp.toPx(),
                            -25.dp.toPx() - (scrollOffset * 0.5f)
                        )
                    }
                    .size(150.dp)
                    .align(Alignment.TopEnd),
                tint = Color.Unspecified
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                contentPadding = PaddingValues(top = 24.dp, bottom = 50.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item("CaloriesInTakeSection") {
                    CaloriesInTakeSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        weekCalenderPagerState = weekCalenderPagerState,
                        goalCalories = state.goalCalories,
                        eatenCalories = state.eatenCalories,
                        eatenFatGrams = state.eatenFatGrams,
                        eatenCarbsGrams = state.eatenCarbsGrams,
                        eatenProteinGrams = state.eatenProteinGrams,
                        goalFatGrams = state.goalFatGrams,
                        goalCarbsGrams = state.goalCarbsGrams,
                        goalProteinGrams = state.goalProteinGrams
                    )
                }
                if (state.foods.isNotEmpty()) {
                    item("meals_header") {
                        Text(
                            text = stringResource(R.string.recently_eaten),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                    items(state.foods, key = { it.id }, contentType = {
                        "meal_${it.id}"
                    }) { meal ->
                        MealCard(
                            meal = meal,
                            onMealClick = { meal ->
                                navHostController.navigate(Destination.MealDetailScreen(meal))
                            },
                            onDeleteMeal = { meal ->
                                viewModel.onAction(DiaryAction.OnSelectMeal(meal))
                                viewModel.onAction(DiaryAction.OnToggleDeleteMealDialog)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .animateItem(),
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        translationY =
                            (-30.dp.toPx() + (scrollOffset * 0.5f)).coerceIn(-30.dp.toPx(), 0f)
                    }
                    .fillMaxWidth()
                    .height(30.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.background.copy(0.8f),
                                MaterialTheme.colorScheme.background.copy(0.7f),
                                MaterialTheme.colorScheme.background.copy(0.6f),
                                MaterialTheme.colorScheme.background.copy(0.5f),
                                Color.Transparent
                            )
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background.copy(0.5f),
                                MaterialTheme.colorScheme.background.copy(0.6f),
                                MaterialTheme.colorScheme.background.copy(0.7f),
                                MaterialTheme.colorScheme.background.copy(0.8f),
                            )
                        )
                    )
            )
        }
    }
    AppBottomSheet(
        showBottomSheet = state.showAddFoodBottomSheet,
        onDismissRequest = {
            viewModel.onAction(DiaryAction.OnToggleAddFoodBottomSheet)
        },
        content = {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                maxItemsInEachRow = 2,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                AddFoodOptionCard(
                    modifier = Modifier.weight(1f),
                    label = R.string.scan_meal,
                    icon = R.drawable.ic_scan_meal,
                    onClick = {
                        viewModel.onAction(DiaryAction.OnToggleAddFoodBottomSheet)
                        navHostController.navigate(Destination.ScanMealScreen())
                    })
                AddFoodOptionCard(
                    modifier = Modifier.weight(1f),
                    label = R.string.gallery,
                    icon = R.drawable.ic_gallery,
                    onClick = {
                        viewModel.onAction(DiaryAction.OnToggleAddFoodBottomSheet)
                        galleryPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    })
                AddFoodOptionCard(
                    modifier = Modifier.weight(1f),
                    label = R.string.food_database,
                    icon = R.drawable.ic_diary,
                    onClick = {
                        viewModel.onAction(DiaryAction.OnToggleAddFoodBottomSheet)

                    })
                AddFoodOptionCard(
                    modifier = Modifier.weight(1f),
                    label = R.string.favourites,
                    icon = R.drawable.ic_favourite_filled,
                    onClick = {
                        viewModel.onAction(DiaryAction.OnToggleAddFoodBottomSheet)
                        navHostController.navigate(Destination.FavouriteMealsScreen)
                    })
            }
        })
    if (state.showDeleteMealDialog) {
        AppAlertDialog(
            onDismiss = {
                viewModel.onAction(DiaryAction.OnToggleDeleteMealDialog)
            },
            title = stringResource(R.string.delete_meal),
            message = stringResource(R.string.delete_meal_desp),
            confirmText = stringResource(R.string.delete),
            dismissText = stringResource(R.string.cancel),
            onConfirmClick = {
                viewModel.onAction(DiaryAction.OnToggleDeleteMealDialog)
                viewModel.onAction(DiaryAction.OnDeleteMeal)
            }, onDismissClick = {
                viewModel.onAction(DiaryAction.OnToggleDeleteMealDialog)
            }
        )
    }
}