package com.muhammad.nutribot.presentation.screens.favourite_meals

sealed interface FavouriteMealsEvent{
    data object OnMealLoggedSuccess : FavouriteMealsEvent
}