package com.muhammad.nutribot.presentation.navigation

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.domain.model.Ingredient
import com.muhammad.nutribot.presentation.screens.diary.DiaryScreen
import com.muhammad.nutribot.presentation.screens.meal_details.MealDetailScreen
import com.muhammad.nutribot.presentation.screens.nurition_setup.NutritionSetupScreen
import com.muhammad.nutribot.presentation.screens.scan_meal.ScanMealScreen
import com.muhammad.nutribot.presentation.screens.setting.SettingScreen
import com.muhammad.nutribot.presentation.screens.streak.StreakScreen
import com.muhammad.nutribot.presentation.screens.welcome.WelcomeScreen
import kotlin.reflect.typeOf

@Composable
fun AppNavigation(
    navController: NavHostController, isUserLoggedIn: Boolean, isInternetConnected: Boolean,
) {
    val startDestination =
        if (isUserLoggedIn) Destination.DiaryScreen else Destination.WelcomeScreen
    SharedTransitionLayout {
        NavHost(navController = navController, startDestination = startDestination) {
            composable<Destination.WelcomeScreen> {
                WelcomeScreen(navController = navController)
            }
            composable<Destination.NutritionSetupScreen> {
                NutritionSetupScreen(navHostController = navController)
            }
            composable<Destination.BoardingScreen> {

            }
            composable<Destination.DiaryScreen> {
                DiaryScreen(
                    navHostController = navController,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this
                )
            }
            composable<Destination.SettingScreen> {
                SettingScreen(navHostController = navController)
            }
            composable<Destination.ScanMealScreen> {
                ScanMealScreen(
                    navHostController = navController,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    isInternetConnected = isInternetConnected
                )
            }
            composable<Destination.ProgressScreen> {

            }
            composable<Destination.StreakScreen> {
                StreakScreen(navHostController = navController)
            }
            composable<Destination.StreakProgressScreen> {

            }
            composable<Destination.MealDetailScreen>(
                typeMap = mapOf(typeOf<Food>() to CustomNavTypes.Food)
            ) {
                MealDetailScreen(
                    navHostController = navController,
                    animatedVisibilityScope = this,
                    sharedTransitionScope = this@SharedTransitionLayout
                )
            }
        }
    }
}