package com.muhammad.nutribot.presentation.screens.meal_details

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.domain.repository.food.FoodRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.serialization.json.Json
import kotlin.time.Instant

class MealDetailViewModel(
    saveStateHandle: SavedStateHandle,
    private val foodRepository: FoodRepository,
) : ViewModel() {
    private val food = Json.decodeFromString<Food>(Uri.decode(checkNotNull(saveStateHandle["food"])))
    private val _state = MutableStateFlow(MealDetailState(food = food))
    val state = _state.asStateFlow()
    private val _events = Channel<MealDetailEvent>()
    val events = _events.receiveAsFlow()
    fun onAction(action: MealDetailAction) {
        when (action) {
            is MealDetailAction.OnChangeSelectedDate -> onChangeSelectedDate(action.date)
            MealDetailAction.OnToggleDatePickerDialog -> onToggleDatePickerDialog()
            is MealDetailAction.OnToggleIngredientSelection -> onToggleIngredientSelection(action.id)
            MealDetailAction.OnToggleMealFavourite -> onToggleMealFavourite()
            is MealDetailAction.OnChangeMealNumberOfServings -> onChangeMealNumberOfServings(action.numberOfServings)
            MealDetailAction.OnLogToMeal -> onLogToMeal()
        }
    }

    private fun onLogToMeal() {
        viewModelScope.launch(Dispatchers.IO) {
            val eatenAt = state.value.selectedDate.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
            foodRepository.upsertFood(food = state.value.food.copy(eatenAt = eatenAt))
            withContext(Dispatchers.Main){
                _events.send(MealDetailEvent.OnMealLoggedSuccess)
            }
        }
    }

    private fun onChangeMealNumberOfServings(numberOfServings: Int) {
        _state.update { currentState ->
            currentState.copy(
                food = currentState.food.copy(
                    numberOfServings = numberOfServings,
                    calories = food.calories * numberOfServings,
                    protein = food.protein * numberOfServings,
                    fat = food.fat * numberOfServings,
                    carbs = food.carbs * numberOfServings
                )
            )
        }
    }

    private fun onToggleMealFavourite() {
        val food = _state.value.food
        _state.update { it.copy(food = food.copy(isFavorite = !food.isFavorite)) }
        if (food.eatenAt != 0L) {
            viewModelScope.launch(Dispatchers.IO) {
                foodRepository.updateFoodFavourite(id = food.id, isFavourite = food.isFavorite)
            }
        }
    }

    private fun onToggleIngredientSelection(id: Long) {
        _state.update { currentState ->
            currentState.copy(
                food = currentState.food.copy(
                ingredients = currentState.food.ingredients.map { ingredient ->
                    if (ingredient.id == id) {
                        ingredient.copy(isSelected = !ingredient.isSelected)
                    } else ingredient
                }
            ))
        }
    }

    private fun onToggleDatePickerDialog() {
        _state.update { it.copy(showDatePickerDialog = !it.showDatePickerDialog) }
    }

    private fun onChangeSelectedDate(date: LocalDate) {
        _state.update { it.copy(selectedDate = date) }
    }
}