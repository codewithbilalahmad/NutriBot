package com.muhammad.nutribot.presentation.screens.search_meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammad.nutribot.data.local.mappers.toFoodEntity
import com.muhammad.nutribot.data.local.mappers.toHistoryFood
import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.domain.repository.food_history.FoodHistoryRespository
import com.muhammad.nutribot.domain.repository.search_food.SearchFoodRespository
import com.muhammad.nutribot.utils.onError
import com.muhammad.nutribot.utils.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchMealViewModel(
    private val foodHistoryRespository: FoodHistoryRespository,
    private val searchFoodRepository: SearchFoodRespository,
) : ViewModel() {
    private var searchJob: Job? = null
    private val _state = MutableStateFlow(SearchMealState())
    val state = combine(
        _state,
        foodHistoryRespository.getAllHistoryFoods()
    ) { state, historyFoods ->
        state.copy(historyFoods = historyFoods, isLoadingHistoryFoods = false)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), SearchMealState())
    private val _events = Channel<SearchMealEvent>()
    val events = _events.receiveAsFlow()
    fun onAction(action: SearchMealAction) {
        when (action) {
            is SearchMealAction.OnSearchFoods -> onSearchFoods(action.query)
            is SearchMealAction.OnSearchMealClick -> onSearchMealClick(action.meal)
        }
    }

    private fun onSearchMealClick(meal: Food) {
        viewModelScope.launch(Dispatchers.IO){
            foodHistoryRespository.upsertHistoryFood(meal.copy(eatenAt = System.currentTimeMillis()).toHistoryFood())
            withContext(Dispatchers.Main){
                _events.trySend(SearchMealEvent.OnSearchMealAdded(meal))
            }
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
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoadingSearchedFood = true,
                    searchFoodsError = null,
                    isSearchFoodsError = false
                )
            }
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