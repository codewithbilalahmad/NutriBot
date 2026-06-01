package com.muhammad.nutribot.presentation.screens.search_meal

import com.muhammad.nutribot.domain.model.Food

sealed interface SearchMealEvent{
    data class OnSearchMealAdded(val meal : Food) : SearchMealEvent
}