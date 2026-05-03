package com.muhammad.nutribot.presentation.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.muhammad.nutribot.presentation.screens.nurition_setup.NuritionSetupScreen
import com.muhammad.nutribot.presentation.screens.welcome.WelcomeScreen

@Composable
fun AppNavigation(
    navController:  NavHostController
) {
    NavHost(navController = navController, startDestination = Destination.WelcomeScreen){
        composable<Destination.WelcomeScreen>{
            WelcomeScreen(navController = navController)
        }
        composable<Destination.NuritionSetupScreen>{
            NuritionSetupScreen(navHostController = navController)
        }
        composable<Destination.BoardingScreen>{

        }
    }
}