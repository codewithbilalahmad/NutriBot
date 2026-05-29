package com.muhammad.nutribot.presentation.screens.favourite_meals

import com.muhammad.nutribot.domain.model.Food

data class FavouriteMealsState(
    val isLoadingMeals : Boolean = false,
    val favouriteMeals: List<Food> = emptyList(),
)
