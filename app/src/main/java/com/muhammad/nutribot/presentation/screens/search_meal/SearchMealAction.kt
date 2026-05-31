package com.muhammad.nutribot.presentation.screens.search_meal

sealed interface SearchMealAction{
    data class OnSearchFoods(val query : String) : SearchMealAction
}