package com.muhammad.nutribot.presentation.screens.meal_details

import com.muhammad.nutribot.domain.model.EditMealOption
import kotlinx.datetime.LocalDate

sealed interface MealDetailAction{
    data class OnChangeSelectedDate(val date: LocalDate): MealDetailAction
    data object OnToggleDatePickerDialog : MealDetailAction
    data class OnToggleIngredientSelection(val id : Long) : MealDetailAction
    data class OnToggleMealFavourite(val favourite : Boolean?=null) : MealDetailAction
    data class OnChangeMealNumberOfServings(val numberOfServings : Int) : MealDetailAction
    data object OnLogToMeal : MealDetailAction
    data class OnEditMealOptionClick(val option : EditMealOption) : MealDetailAction
    data object OnSaveEditMealOption : MealDetailAction
    data object OnDismissEditMealDialog : MealDetailAction
    data object OnClearEditMealState : MealDetailAction
}