package com.muhammad.nutribot.presentation.screens.diary

import kotlinx.datetime.LocalDate

sealed interface DiaryAction{
    data class OnDateSelected(val date : LocalDate) : DiaryAction
}