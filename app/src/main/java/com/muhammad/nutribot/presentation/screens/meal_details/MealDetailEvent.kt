package com.muhammad.nutribot.presentation.screens.meal_details

sealed interface MealDetailEvent{
    data object OnMealLoggedSuccess : MealDetailEvent
}