package com.muhammad.nutribot.presentation.navigation

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.domain.model.Ingredient
import com.muhammad.nutribot.presentation.screens.diary.DiaryScreen
import com.muhammad.nutribot.presentation.screens.meal_details.MealDetailScreen
import com.muhammad.nutribot.presentation.screens.nurition_setup.NutritionSetupScreen
import com.muhammad.nutribot.presentation.screens.scan_meal.ScanMealScreen
import com.muhammad.nutribot.presentation.screens.setting.SettingScreen
import com.muhammad.nutribot.presentation.screens.welcome.WelcomeScreen
import kotlin.reflect.typeOf

@Composable
fun AppNavigation(
    navController: NavHostController, isUserLoggedIn: Boolean, isInternetConnected: Boolean,
) {
    val startDestination = if (isUserLoggedIn) Destination.MealDetailScreen(food = Food(id=6623597361860987959, name= "Beef Curry", mealImageUrl="/data/user/0/com.muhammad.nutribot/cache/meal_1779284163736.png", calories=450, protein=35, fat=25, carbs=15, numberOfServings=1, eatenAt=1779284163863, confidenceScore=90, ingredients=listOf(Ingredient(id=894940839906068697, foodId=6623597361860987959, name="Beef", calories=300, protein=30, fat=20, carbs=0, isSelected=true), Ingredient(id=5888185990940345621, foodId=6623597361860987959, name="Curry Sauce", calories=100, protein=3, fat=4, carbs=12, isSelected=true), Ingredient(
        id = 2633154388371787560,
        foodId = 6623597361860987959,
        name = "Cilantro",
        calories = 5,
        protein = 1,
        fat = 0,
        carbs = 3,
        isSelected = true
    )), isFavorite=false)) else Destination.WelcomeScreen
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
                DiaryScreen(navHostController = navController)
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
            composable<Destination.MealDetailScreen>(
                typeMap = mapOf(typeOf<Food>() to CustomNavTypes.Food)
            ) {
                val args = it.toRoute<Destination.MealDetailScreen>()
                MealDetailScreen(
                    food = args.food,
                    navHostController = navController,
                    animatedVisibilityScope = this,
                    sharedTransitionScope = this@SharedTransitionLayout
                )
            }
        }
    }
}