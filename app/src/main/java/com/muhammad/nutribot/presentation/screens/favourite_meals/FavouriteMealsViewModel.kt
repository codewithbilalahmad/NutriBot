package com.muhammad.nutribot.presentation.screens.favourite_meals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammad.nutribot.domain.repository.food.FoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class FavouriteMealsViewModel(
   private val foodRepository: FoodRepository
) : ViewModel(){
    private val _state = MutableStateFlow(FavouriteMealsState())
    val state = combine(
        _state,
        foodRepository.getFavouriteFoods()
    ){state, favouriteMeals ->
        state.copy(favouriteMeals = favouriteMeals, isLoadingMeals = false)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), FavouriteMealsState())
}