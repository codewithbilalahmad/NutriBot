package com.muhammad.nutribot.presentation.screens.favourite_meals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.domain.repository.food.FoodRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouriteMealsViewModel(
    private val foodRepository: FoodRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(FavouriteMealsState())
    val state = combine(
        _state,
        foodRepository.getFavouriteFoods()
    ) { state, favouriteMeals ->
        state.copy(favouriteMeals = favouriteMeals, isLoadingMeals = false)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), FavouriteMealsState())
    private val _events = Channel<FavouriteMealsEvent>()
    val events = _events.receiveAsFlow()
    fun onAction(action: FavouriteMealsAction) {
        when (action) {
            is FavouriteMealsAction.OnLogMeal -> onLogMeal(action.meal)
            is FavouriteMealsAction.OnUnFavouriteMeal -> onUnFavouriteMeal(action.id)
        }
    }

    private fun onUnFavouriteMeal(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            foodRepository.updateFoodFavourite(id = id, isFavourite = false)
        }
    }

    private fun onLogMeal(meal: Food) {
        viewModelScope.launch(Dispatchers.IO) {
            foodRepository.upsertFood(food = meal.copy(id = 0L,eatenAt = System.currentTimeMillis()))
            withContext(Dispatchers.Main){
                _events.trySend(FavouriteMealsEvent.OnMealLoggedSuccess)
            }
        }
    }
}