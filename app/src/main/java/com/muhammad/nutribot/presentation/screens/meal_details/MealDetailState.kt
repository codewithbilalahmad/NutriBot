package com.muhammad.nutribot.presentation.screens.meal_details

import androidx.compose.foundation.text.input.TextFieldState
import com.muhammad.nutribot.domain.model.EditMealOption
import com.muhammad.nutribot.domain.model.Food
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock


data class MealDetailState(
    val food : Food,
    val selectedDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    val selectedEditMealOption: EditMealOption = EditMealOption.NONE,
    val editMealState : TextFieldState = TextFieldState(),
    val showEditMealDialog : Boolean = false,
    val showDatePickerDialog : Boolean = false
){
    val isAlreadyLogged : Boolean = food.eatenAt != 0L
}
