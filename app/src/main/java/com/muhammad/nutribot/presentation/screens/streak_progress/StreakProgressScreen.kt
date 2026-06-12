package com.muhammad.nutribot.presentation.screens.streak_progress

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.muhammad.nutribot.R
import com.muhammad.nutribot.presentation.components.button.PrimaryButton
import com.muhammad.nutribot.presentation.components.lottie.AppLottieAnimation
import com.muhammad.nutribot.presentation.components.streak_series.StreakSeries
import com.muhammad.nutribot.presentation.navigation.Destination
import com.muhammad.nutribot.presentation.screens.streak_progress.components.FireShader
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StreakProgressScreen(
    navHostController: NavHostController,
    viewModel: StreakProgressViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val streakDescription = remember(state.streak) {
        when (state.streak) {
            0 -> "Your journey is waiting to begin. Start today and build your very first streak!"

            1 -> "Great first step! Keep showing up every day to build strong momentum."

            in 2..3 -> "Nice progress so far! You're slowly building consistency and healthy habits."

            in 4..6 -> "Awesome work! Your dedication is starting to turn into a strong routine."

            in 7..13 -> "Impressive streak! Staying consistent like this is a huge achievement already."

            in 14..29 -> "Excellent dedication! Your discipline and daily effort are clearly paying off."

            in 30..59 -> "Amazing commitment! This habit is becoming a natural part of your lifestyle."

            else -> "Legendary consistency! You've built an incredible streak and you're truly unstoppable now."
        }
    }
    BackHandler {
        navHostController.navigate(Destination.DiaryScreen){
            popUpTo(Destination.StreakProgressScreen){
                inclusive = true
            }
        }
    }
    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            PrimaryButton(
                text = stringResource(R.string.onward_to_goal),
                onClick = {
                    navHostController.navigate(Destination.DiaryScreen){
                        popUpTo(Destination.StreakProgressScreen){
                            inclusive = true
                        }
                    }
                },
                contentPadding = PaddingValues(vertical = 16.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()){
            FireShader(modifier = Modifier.fillMaxSize())
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AppLottieAnimation(
                    modifier = Modifier.size(width = 150.dp, height = 200.dp),
                    lottieId = R.raw.fire_animation
                )
                Text(
                    text = state.streak.toString(),
                    style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.ExtraBold)
                )
                Text(
                    text = "${state.streak} ${stringResource(if (state.streak > 1) R.string.days_on_track else R.string.day_on_track)}",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(Modifier.height(24.dp))
                StreakSeries(weekMeals = state.weekMeals, modifier = Modifier.fillMaxWidth(),showShadow = false)
                Spacer(Modifier.height(12.dp))
                Text(
                    text = streakDescription,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp, lineHeight = 18.sp
                    )
                )
            }
        }
    }
}