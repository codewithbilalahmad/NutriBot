package com.muhammad.nutribot.presentation.screens.meal_details.components

import kotlinx.datetime.LocalDate

sealed interface MealDetailAction{
    data class OnChangeSelectedDate(val date: LocalDate): MealDetailAction
}
