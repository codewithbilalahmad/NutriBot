package com.muhammad.nutribot.presentation.screens.streak

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammad.nutribot.domain.repository.food.FoodRepository
import com.muhammad.nutribot.domain.repository.settings.SettingRepository
import com.muhammad.nutribot.utils.getCurrentWeekMillis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StreakViewModel(
    private val foodRepository: FoodRepository,
    private val settingRepository: SettingRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(StreakState())
    private val weekMillis = getCurrentWeekMillis()
    val state = combine(
        _state,
        foodRepository.getFoodStreak(),
        settingRepository.observeBestStreak(),
        foodRepository.getFoodsByDate(startOfDay = weekMillis.first, endOfDay = weekMillis.second),
    ) { state, streak, bestStreak, weekMeals ->
        state.copy(streak = streak, bestStreak = bestStreak, weekMeals = weekMeals)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), StreakState())

    fun onAction(action : StreakAction){
        when(action){
            StreakAction.OnSaveBestStreak -> onSaveBestStreak()
        }
    }

    private fun onSaveBestStreak() {
        viewModelScope.launch(Dispatchers.IO){
            settingRepository.saveBestStreak(state.value.streak)
        }
    }
}