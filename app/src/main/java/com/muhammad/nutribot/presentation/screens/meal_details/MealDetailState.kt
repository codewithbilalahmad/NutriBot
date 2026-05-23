package com.muhammad.nutribot.presentation.screens.meal_details

import com.muhammad.nutribot.domain.model.Food
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock


data class MealDetailState(
    val food : Food,
    val selectedDate: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
    val showDatePickerDialog : Boolean = false
)
