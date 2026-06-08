package com.muhammad.nutribot.presentation.screens.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.domain.repository.food.FoodRepository
import com.muhammad.nutribot.domain.repository.settings.SettingRepository
import com.muhammad.nutribot.utils.endOdDayMillis
import com.muhammad.nutribot.utils.firstDayOfMonth
import com.muhammad.nutribot.utils.lastDayOfMonth
import com.muhammad.nutribot.utils.startOfDayMillis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

class ProgressViewModel(
    private val foodRepository: FoodRepository,
    private val settingsRepository: SettingRepository,
) : ViewModel() {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    private val _state = MutableStateFlow(ProgressState())
    val state = combine(
        _state,
        foodRepository.getFoodStreak(),
        settingsRepository.observeNutritionCalculation(),
        foodRepository.getFoodsByDate(startOfDay = today.minus(DatePeriod(days = 5)).startOfDayMillis(), endOfDay = today.plus(DatePeriod(days = 1)).endOdDayMillis()),
        foodRepository.getFoodsByDate(startOfDay = today.firstDayOfMonth().startOfDayMillis(), endOfDay = today.lastDayOfMonth().endOdDayMillis())
    ) { state, streak,nutritionCalculation, weekMeals, monthMeals ->
        state.copy(
            streak = streak,
            nutritionCalculation = nutritionCalculation,
            weekMeals = run {
                val mealsByDate = weekMeals.groupByLocalDate()
                (0..6)
                    .map { today.minus(DatePeriod(days = 6 - it)) }
                    .associateWith { date ->
                        mealsByDate[date] ?: emptyList()
                    }
            }, monthMeals = run {
                val mealsByDate = monthMeals.groupByLocalDate()
                val start = today.firstDayOfMonth()
                val end = today.lastDayOfMonth()
                generateSequence(start){current ->
                    val next = current.plus(DatePeriod(days = 1))
                    if(next <= end) next else null
                }.associateWith { date ->
                    mealsByDate[date] ?: emptyList()
                }
            }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ProgressState())

    fun onAction(action: ProgressAction){
        when(action){
            ProgressAction.OnToggleAddFoodBottomSheet -> onToggleAddFoodBottomSheet()
        }
    }

    private fun onToggleAddFoodBottomSheet() {
        _state.update { it.copy(showAddFoodBottomSheet = !it.showAddFoodBottomSheet) }
    }
    private fun  List<Food>.groupByLocalDate(): Map<LocalDate, List<Food>> {
        return groupBy {
            Instant.fromEpochMilliseconds(it.eatenAt)
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date
        }
    }
}