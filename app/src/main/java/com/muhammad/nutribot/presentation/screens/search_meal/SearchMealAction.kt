package com.muhammad.nutribot.presentation.screens.search_meal

import com.muhammad.nutribot.domain.model.Food

sealed interface SearchMealAction{
    data class OnSearchFoods(val query : String) : SearchMealAction
    data class OnSearchMealClick(val meal : Food) : SearchMealAction
}