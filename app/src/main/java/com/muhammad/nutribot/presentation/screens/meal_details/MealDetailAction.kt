package com.muhammad.nutribot.presentation.screens.meal_details

import kotlinx.datetime.LocalDate

sealed interface MealDetailAction{
    data class OnChangeSelectedDate(val date: LocalDate): MealDetailAction
    data object OnToggleDatePickerDialog : MealDetailAction
    data class OnToggleIngredientSelection(val id : Long) : MealDetailAction
    data class OnToggleMealFavourite(val favourite : Boolean?=null) : MealDetailAction
    data class OnChangeMealNumberOfServings(val numberOfServings : Int) : MealDetailAction
    data object OnLogToMeal : MealDetailAction
}