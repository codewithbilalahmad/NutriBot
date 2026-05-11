package com.muhammad.nutribot.presentation.screens.diary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.muhammad.nutribot.R
import com.muhammad.nutribot.presentation.components.bottom_sheet.AppBottomSheet
import com.muhammad.nutribot.presentation.navigation.BottomNavigationBar
import com.muhammad.nutribot.presentation.navigation.Destination
import com.muhammad.nutribot.presentation.screens.diary.components.AddFoodOptionCard
import com.muhammad.nutribot.presentation.screens.diary.components.CaloriesInTakeSection
import com.muhammad.nutribot.presentation.screens.diary.components.DiaryTopbar
import com.muhammad.nutribot.presentation.screens.diary.components.EmptyFoodSection
import org.koin.androidx.compose.koinViewModel

@Composable
fun DiaryScreen(navHostController: NavHostController, viewModel: DiaryViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val weekRange = state.weekRange
    val weekCalenderPagerState =
        rememberPagerState(initialPage = weekRange.count() - 1) { weekRange.count() }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        DiaryTopbar(
            modifier = Modifier.fillMaxWidth(),
            onSettingClick = {
                navHostController.navigate(Destination.SettingScreen)
            },
            onDateSelected = { date ->
                viewModel.onAction(DiaryAction.OnDateSelected(date))
            },
            foods = state.foods,
            streak = state.streak,
            weekRange = weekRange,
            weekCalenderPagerState = weekCalenderPagerState,
            selectedDate = state.selectedDate,
        )
    }, bottomBar = {
        BottomNavigationBar(navHostController = navHostController, onAddFoodClick = {
            viewModel.onAction(DiaryAction.OnToggleAddFoodBottomSheet)
        })
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 24.dp, bottom = 32.dp),
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
                item("EmptyFoodSection") {
                    EmptyFoodSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .animateItem()
                    )
                }
            }
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
                    onClick = {})
                AddFoodOptionCard(
                    modifier = Modifier.weight(1f),
                    label = R.string.gallery,
                    icon = R.drawable.ic_gallery,
                    onClick = {})
                AddFoodOptionCard(
                    modifier = Modifier.weight(1f),
                    label = R.string.food_database,
                    icon = R.drawable.ic_diary,
                    onClick = {})
                AddFoodOptionCard(
                    modifier = Modifier.weight(1f),
                    label = R.string.favourites,
                    icon = R.drawable.ic_favourite_filled,
                    onClick = {})
            }
        })
}