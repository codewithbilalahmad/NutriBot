package com.muhammad.nutribot.presentation.screens.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.domain.repository.food.FoodRepository
import com.muhammad.nutribot.domain.repository.settings.SettingRepository
import com.muhammad.nutribot.utils.endOdDayMillis
import com.muhammad.nutribot.utils.startOfDayMillis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class DiaryViewModel(
    private val foodRepository: FoodRepository,
    private val settingRepository: SettingRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(DiaryState())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val foodsFlow = _state
        .map { it.selectedDate }
        .distinctUntilChanged()
        .flatMapLatest { date ->
            foodRepository.getFoodsByDate(
                startOfDay = date.startOfDayMillis(),
                endOfDay = date.endOdDayMillis()
            )
        }

    val state = combine(
        _state,
        settingRepository.observeNutritionCalculation(),
        foodRepository.getFoodStreak(),
        foodsFlow,
        foodRepository.getAllFoods()
    ) { state, nutritionCalculation, streak, foods, allFoods ->
        println("Meals : $foods")
        state.copy(
            foods = foods,
            isLoadingFoods = false,
            allFoods = allFoods,
            streak = streak, nutritionCalculation = nutritionCalculation,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), DiaryState())

    fun onAction(action: DiaryAction) {
        when (action) {
            is DiaryAction.OnDateSelected -> onDateSelected(action.date)
            DiaryAction.OnToggleAddFoodBottomSheet -> onToggleAddFoodBottomSheet()
            DiaryAction.OnDeleteMeal -> onDeleteMeal()
            DiaryAction.OnToggleDeleteMealDialog -> onToggleDeleteMealDialog()
            is DiaryAction.OnSelectMeal -> onSelectMeal(action.meal)
        }
    }

    private fun onSelectMeal(meal: Food) {
        _state.update { it.copy(selectedMeal = meal) }
    }

    private fun onToggleDeleteMealDialog() {
        _state.update { it.copy(showDeleteMealDialog = !it.showDeleteMealDialog) }
    }

    private fun onDeleteMeal() {
        viewModelScope.launch(Dispatchers.IO) {
            val meal = state.value.selectedMeal ?: return@launch
            foodRepository.deleteFood(meal.id)
        }
    }

    private fun onToggleAddFoodBottomSheet() {
        _state.update { it.copy(showAddFoodBottomSheet = !it.showAddFoodBottomSheet) }
    }

    private fun onDateSelected(date: LocalDate) {
        _state.update { it.copy(selectedDate = date) }
    }
}