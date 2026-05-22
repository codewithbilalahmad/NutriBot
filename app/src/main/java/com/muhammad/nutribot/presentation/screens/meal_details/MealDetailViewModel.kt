package com.muhammad.nutribot.presentation.screens.meal_details

import androidx.lifecycle.ViewModel
import com.muhammad.nutribot.presentation.screens.meal_details.components.MealDetailAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate

class MealDetailViewModel() : ViewModel(){
    private val _state = MutableStateFlow(MealDetailState())
    val state = _state.asStateFlow()
    fun onAction(action: MealDetailAction){
        when(action){
            is MealDetailAction.OnChangeSelectedDate -> onChangeSelectedDate(action.date)
        }
    }

    private fun onChangeSelectedDate(date: LocalDate) {
        _state.update { it.copy(selectedDate = date) }
    }
}