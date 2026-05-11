package com.muhammad.nutribot.presentation.screens.scan_meal

import com.muhammad.nutribot.domain.model.Food

sealed interface ScanMealEvent{
    data class OnMealAnalyzedSuccess(val food: Food) : ScanMealEvent
}