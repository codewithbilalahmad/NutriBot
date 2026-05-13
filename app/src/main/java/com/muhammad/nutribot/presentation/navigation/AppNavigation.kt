package com.muhammad.nutribot.presentation.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.muhammad.nutribot.presentation.screens.diary.DiaryScreen
import com.muhammad.nutribot.presentation.screens.nurition_setup.NutritionSetupScreen
import com.muhammad.nutribot.presentation.screens.scan_meal.ScanMealScreen
import com.muhammad.nutribot.presentation.screens.setting.SettingScreen
import com.muhammad.nutribot.presentation.screens.welcome.WelcomeScreen

@Composable
fun AppNavigation(
    navController: NavHostController, isUserLoggedIn: Boolean, isInternetConnected: Boolean,
) {
    val startDestination =
        if (isUserLoggedIn) Destination.DiaryScreen else Destination.WelcomeScreen
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
            DiaryScreen(navHostController = navController)
        }
        composable<Destination.SettingScreen> {
            SettingScreen(navHostController = navController)
        }
        composable<Destination.ScanMealScreen> {
            ScanMealScreen(
                navHostController = navController,
                isInternetConnected = isInternetConnected
            )
        }
        composable<Destination.ProgressScreen> {

        }
    }
}