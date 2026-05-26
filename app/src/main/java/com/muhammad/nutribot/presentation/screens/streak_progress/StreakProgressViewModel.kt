package com.muhammad.nutribot.presentation.screens.streak_progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammad.nutribot.domain.repository.food.FoodRepository
import com.muhammad.nutribot.utils.getCurrentWeekMillis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class StreakProgressViewModel(
    private val foodRepository: FoodRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(StreakProgressState())
    private val currentWeekMillis = getCurrentWeekMillis()
    val state = combine(
        _state,
        foodRepository.getFoodStreak(),
        foodRepository.getFoodsByDate(
            startOfDay = currentWeekMillis.first,
            endOfDay = currentWeekMillis.second
        )
    ){state, streak, weekMeals ->
        state.copy(streak = streak, weekMeals = weekMeals)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), StreakProgressState())
}