package com.muhammad.nutribot.presentation.screens.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammad.nutribot.domain.repository.food.FoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ProgressViewModel(
    private val foodRepository: FoodRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ProgressState())
    val state = combine(
        _state, foodRepository.getFoodStreak()
    ) { state, streak ->
        state.copy(streak = streak)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ProgressState())
}