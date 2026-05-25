package com.muhammad.nutribot.presentation.screens.streak

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.muhammad.nutribot.R
import com.muhammad.nutribot.presentation.components.button.PrimaryButton
import com.muhammad.nutribot.presentation.components.streak_series.StreakSeries
import com.muhammad.nutribot.presentation.screens.streak.components.BestStreakCard
import com.muhammad.nutribot.presentation.screens.streak.components.StreakGoalBar
import com.muhammad.nutribot.presentation.screens.streak.components.StreakHeader
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StreakScreen(
    navHostController: NavHostController,
    viewModel: StreakViewModel = koinViewModel(),
) {
    val layoutDirection = LocalLayoutDirection.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.streak) {
        if (state.streak > state.bestStreak) {
            viewModel.onAction(StreakAction.OnSaveBestStreak)
        }
    }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        StreakHeader(modifier = Modifier.fillMaxWidth(), streak = state.streak, onBackClick = {
            navHostController.navigateUp()
        })
    }, bottomBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            PrimaryButton(
                text = stringResource(R.string.onward_to_goal),
                onClick = {
                },
                contentPadding = PaddingValues(vertical = 16.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = paddingValues.calculateStartPadding(layoutDirection) + 16.dp,
                end = paddingValues.calculateEndPadding(layoutDirection) + 16.dp,
                top = paddingValues.calculateTopPadding() + 24.dp,
                bottom = paddingValues.calculateTopPadding() + 50.dp,
            )
        ) {
            item("streak_series_title") {
                Text(
                    text = stringResource(R.string.streak_series),
                    modifier = Modifier.animateItem(),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                )
            }
            item("streak_series") {
                StreakSeries(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem()
                        .padding(top = 12.dp), weekMeals = state.weekMeals
                )
            }
            item("streak_goal_title") {
                Text(
                    text = stringResource(R.string.streak_goal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem()
                        .padding(top = 24.dp),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                )
            }
            item("streak_goal_bar") {
                StreakGoalBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem()
                        .padding(top = 16.dp),
                    streak = state.streak
                )
            }
            item("BestStreakCard") {
                BestStreakCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                        .animateItem(),
                    bestStreak = state.bestStreak
                )
            }
        }
    }
}