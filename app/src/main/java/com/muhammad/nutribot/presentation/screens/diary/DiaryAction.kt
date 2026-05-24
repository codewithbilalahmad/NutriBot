package com.muhammad.nutribot.presentation.screens.diary

import com.muhammad.nutribot.domain.model.Food
import kotlinx.datetime.LocalDate

sealed interface DiaryAction{
    data class OnDateSelected(val date : LocalDate) : DiaryAction
    data object OnToggleAddFoodBottomSheet : DiaryAction
    data class OnSelectMeal(val meal : Food) : DiaryAction
    data object OnToggleDeleteMealDialog : DiaryAction
    data object OnDeleteMeal : DiaryAction
}