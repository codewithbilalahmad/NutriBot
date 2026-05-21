package com.muhammad.nutribot.presentation.screens.meal_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MealDetailViewModel(
    saveStateHandle: SavedStateHandle
) : ViewModel(){
    private val _state = MutableStateFlow(MealDetailState())
    val state = _state.asStateFlow()
}