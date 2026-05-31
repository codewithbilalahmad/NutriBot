package com.muhammad.nutribot.presentation.screens.search_meal

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable
import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.domain.model.HistoryFood

@Immutable
data class SearchMealState(
    val historyFoods : List<HistoryFood> = emptyList(),
    val searchedFood : List<Food> = emptyList(),
    val isLoadingHistoryFoods : Boolean = true,
    val isLoadingSearchedFood : Boolean = false,
    val searchFoodsError : String?=null,
    val isSearchFoodsError : Boolean = false,
    val query : TextFieldState = TextFieldState()
)