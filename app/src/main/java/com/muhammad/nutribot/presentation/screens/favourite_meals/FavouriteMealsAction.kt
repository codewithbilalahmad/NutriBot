package com.muhammad.nutribot.presentation.screens.favourite_meals

import com.muhammad.nutribot.domain.model.Food

sealed interface FavouriteMealsAction{
    data class OnUnFavouriteMeal(val id : Long) : FavouriteMealsAction
    data class OnLogMeal(val meal : Food) : FavouriteMealsAction
}