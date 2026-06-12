package com.muhammad.nutribot.presentation.screens.progress

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.muhammad.nutribot.R
import com.muhammad.nutribot.presentation.components.bottom_sheet.AppBottomSheet
import com.muhammad.nutribot.presentation.components.dashed_divider.DashedHorizontalDivider
import com.muhammad.nutribot.presentation.navigation.BottomNavigationBar
import com.muhammad.nutribot.presentation.navigation.Destination
import com.muhammad.nutribot.presentation.screens.diary.DiaryAction
import com.muhammad.nutribot.presentation.screens.diary.components.AddFoodOptionCard
import com.muhammad.nutribot.presentation.screens.progress.components.MonthlyStreakHeatmapSection
import com.muhammad.nutribot.presentation.screens.progress.components.NuritionCaloriesChart
import com.muhammad.nutribot.presentation.screens.progress.components.NutritionLineChartSection
import com.muhammad.nutribot.presentation.screens.progress.components.ProgressTopbar
import com.muhammad.nutribot.presentation.theme.CarbsColor
import com.muhammad.nutribot.presentation.theme.FatColor
import com.muhammad.nutribot.presentation.theme.ProteinColor
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProgressScreen(
    navHostController: NavHostController,
    viewModel: ProgressViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val galleryPicker =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                navHostController.navigate(Destination.ScanMealScreen(galleryUri = uri.toString()))
            }
        }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        Column(modifier = Modifier.fillMaxWidth()) {
            ProgressTopbar(streak = state.streak, onStreakClick = {
                navHostController.navigate(Destination.StreakScreen)
            }, onSettingClick = {})
            DashedHorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.5.dp)
        }
    }, bottomBar = {
        BottomNavigationBar(navHostController = navHostController, onAddFoodClick = {
            viewModel.onAction(ProgressAction.OnToggleAddFoodBottomSheet)
        }, modifier = Modifier.fillMaxWidth())
    }, containerColor = MaterialTheme.colorScheme.background) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(
                    top = 16.dp,
                    bottom = 50.dp
                ), verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item("MonthlyStreakHeatmapSection") {
                    MonthlyStreakHeatmapSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .animateItem(), monthMeals = state.monthMeals,
                        goalCalories = state.goalCalories
                    )
                }
                item("NuritionCaloriesChart") {
                    NuritionCaloriesChart(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .animateItem(), points = state.nutritionCaloriesPoints,targetCalories = state.goalCalories)
                }
                item("protein_chart") {
                    NutritionLineChartSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        color = ProteinColor,
                        icon = R.drawable.ic_protein,
                        label = R.string.protein,
                        points = state.proteinNuritionPoints
                    )
                }
                item("carbs_chart") {
                    NutritionLineChartSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        icon = R.drawable.ic_carbs,
                        color = CarbsColor,
                        label = R.string.carbs,
                        points = state.carbsNuritionPoints
                    )
                }
                item("fats_chart") {
                    NutritionLineChartSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        icon = R.drawable.ic_fat,
                        color = FatColor,
                        label = R.string.fat,
                        points = state.fatNuritionPoints
                    )
                }
            }
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
            viewModel.onAction(ProgressAction.OnToggleAddFoodBottomSheet)
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
                        viewModel.onAction(ProgressAction.OnToggleAddFoodBottomSheet)
                        navHostController.navigate(Destination.ScanMealScreen())
                    })
                AddFoodOptionCard(
                    modifier = Modifier.weight(1f),
                    label = R.string.gallery,
                    icon = R.drawable.ic_gallery,
                    onClick = {
                        viewModel.onAction(ProgressAction.OnToggleAddFoodBottomSheet)
                        galleryPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    })
                AddFoodOptionCard(
                    modifier = Modifier.weight(1f),
                    label = R.string.food_database,
                    icon = R.drawable.ic_meal,
                    onClick = {
                        viewModel.onAction(ProgressAction.OnToggleAddFoodBottomSheet)
                        navHostController.navigate(Destination.SearchMealScreen)
                    })
                AddFoodOptionCard(
                    modifier = Modifier.weight(1f),
                    label = R.string.favourites,
                    icon = R.drawable.ic_favourite_filled,
                    onClick = {
                        viewModel.onAction(ProgressAction.OnToggleAddFoodBottomSheet)
                        navHostController.navigate(Destination.FavouriteMealsScreen)
                    })
            }
        })
}