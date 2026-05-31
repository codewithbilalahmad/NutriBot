package com.muhammad.nutribot.presentation.screens.search_meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammad.nutribot.domain.repository.food_history.FoodHistoryRespository
import com.muhammad.nutribot.domain.repository.search_food.SearchFoodRespository
import com.muhammad.nutribot.utils.onError
import com.muhammad.nutribot.utils.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchMealViewModel(
    private val foodHistoryRespository: FoodHistoryRespository,
    private val searchFoodRepository: SearchFoodRespository,
) : ViewModel() {
    private val _state = MutableStateFlow(SearchMealState())
    val state = combine(
        _state,
        foodHistoryRespository.getAllHistoryFoods()
    ) { state, historyFoods ->
        state.copy(historyFoods = historyFoods)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), SearchMealState())

    fun onAction(action: SearchMealAction) {
        when (action) {
            is SearchMealAction.OnSearchFoods -> onSearchFoods(action.query)
        }
    }

    private fun onSearchFoods(query: String) {
        if (query.isBlank()) {
            _state.update {
                it.copy(
                    searchedFood = emptyList(),
                    isLoadingSearchedFood = false,
                    isSearchFoodsError = false,
                    searchFoodsError = null
                )
            }
            return
        }
        viewModelScope.launch {
            searchFoodRepository.searchFoods(query).onSuccess { foods ->
                _state.update {
                    it.copy(
                        searchedFood = foods,
                        isLoadingSearchedFood = false,
                        searchFoodsError = null, isSearchFoodsError = false
                    )
                }
            }.onError { error ->
                _state.update {
                    it.copy(
                        isLoadingSearchedFood = false,
                        searchFoodsError = error,
                        isSearchFoodsError = true
                    )
                }
            }
        }
    }
}