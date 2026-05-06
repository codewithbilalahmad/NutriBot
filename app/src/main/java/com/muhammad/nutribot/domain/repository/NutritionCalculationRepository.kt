package com.muhammad.nutribot.domain.repository

import com.muhammad.nutribot.domain.model.ActivityLevel
import com.muhammad.nutribot.domain.model.Gender
import com.muhammad.nutribot.domain.model.MainGoal
import com.muhammad.nutribot.domain.model.NutritionCalculation

interface NutritionCalculationRepository {
    fun calculateNutrition(
        gender: Gender,
        age: Int,
        heightCm: Int,
        weightKg: Int,
        activityLevel: ActivityLevel,
        mainGoals: List<MainGoal>
    ): NutritionCalculation
}