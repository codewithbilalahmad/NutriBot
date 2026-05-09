package com.muhammad.nutribot.presentation.screens.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammad.nutribot.domain.repository.food.FoodRepository
import com.muhammad.nutribot.domain.repository.settings.SettingRepository
import com.muhammad.nutribot.utils.endOdDayMillis
import com.muhammad.nutribot.utils.startOfDayMillis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate

class DiaryViewModel(
    private val foodRepository: FoodRepository,
    private val settingRepository: SettingRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(DiaryState())
    val state = combine(
        _state,
        settingRepository.observeNutritionCalculation(),
        foodRepository.getFoodStreak(),
        foodRepository.getFoodsByDate(
            startOfDay = _state.value.selectedDate.startOfDayMillis(),
            endOfDay = _state.value.selectedDate.endOdDayMillis()
        )
    ){state, nutritionCalculation,streak, foods ->
        state.copy(
            nutritionCalculation = nutritionCalculation,
            streak = streak,
            foods = foods
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), DiaryState())

    fun onAction(action: DiaryAction){
        when(action){
            is DiaryAction.OnDateSelected -> onDateSelected(action.date)
        }
    }

    private fun onDateSelected(date: LocalDate) {
        _state.update { it.copy(selectedDate = date) }
    }
}