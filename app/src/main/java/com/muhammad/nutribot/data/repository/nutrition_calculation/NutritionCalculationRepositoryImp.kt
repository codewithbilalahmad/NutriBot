package com.muhammad.nutribot.data.repository.nutrition_calculation

import com.muhammad.nutribot.domain.model.ActivityLevel
import com.muhammad.nutribot.domain.model.Gender
import com.muhammad.nutribot.domain.model.MainGoal
import com.muhammad.nutribot.domain.model.NutritionCalculation
import com.muhammad.nutribot.domain.repository.nutrition_calculation.NutritionCalculationRepository

class NutritionCalculationRepositoryImp : NutritionCalculationRepository {
    override fun calculateNutrition(
        gender: Gender,
        age: Int,
        heightCm: Int,
        weightKg: Int,
        activityLevel: ActivityLevel,
        mainGoals: List<MainGoal>,
    ): NutritionCalculation {
        val bmr = calculateBMR(gender = gender, weightKg = weightKg, heightCm = heightCm, age = age)
        val tdee = bmr * when (activityLevel) {
            ActivityLevel.SEDENTARY -> 1.2
            ActivityLevel.LIGHTLY_ACTIVE -> 1.375
            ActivityLevel.MODERATELY_ACTIVE -> 1.55
            ActivityLevel.VERY_ACTIVE -> 1.725
            ActivityLevel.SUPER_ACTIVE -> 1.9
        }
        val goalCalories = when (mainGoals.firstOrNull()) {
            MainGoal.LOSE_WEIGHT -> tdee - 400
            MainGoal.GAIN_WEIGHT -> tdee + 400
            MainGoal.GAIN_MUSCLE -> tdee + 300
            else -> tdee
        }.toInt()
        val (proteinPercent, carbsPercent, fatPercent) = getNuritionRatios(mainGoals)
        val proteinCalories = goalCalories * proteinPercent
        val carbsCalories = goalCalories * carbsPercent
        val fatCalories = goalCalories * fatPercent

        val proteinGrams = (proteinCalories / 4).toInt()
        val carbsGrams = (carbsCalories / 4).toInt()
        val fatGrams = (fatCalories / 9).toInt()
        return NutritionCalculation(
            calories = goalCalories,
            proteinPercent = (proteinPercent * 100).toInt(),
            carbsPercent = (carbsPercent * 100).toInt(),
            fatPercent = (fatPercent * 100).toInt(),
            proteinGrams = proteinGrams,
            carbsGrams = carbsGrams,
            fatGrams = fatGrams
        )
    }

    private fun calculateBMR(
        gender: Gender,
        weightKg: Int,
        heightCm: Int,
        age: Int,
    ): Double {
        return when (gender) {
            Gender.MALE -> 10 * weightKg + 6.25 * heightCm - 5 * age + 5
            Gender.FEMALE -> 10 * weightKg + 6.25 * heightCm - 5 * age - 161
            Gender.PREFER_NOT_TO_SAY ->{
                val male = 10 * weightKg + 6.25 * heightCm - 5 * age + 5
                val female = 10 * weightKg + 6.25 * heightCm - 5 * age - 161
                (male + female) / 2
            }
        }
    }

    private fun getNuritionRatios(goals: List<MainGoal>): Triple<Double, Double, Double> {
        val ratios = goals.map { goal ->
            when (goal) {
                MainGoal.LOSE_WEIGHT -> Triple(0.30, 0.40, 0.30)
                MainGoal.GAIN_MUSCLE -> Triple(0.30, 0.45, 0.25)
                MainGoal.MAINTAIN_WEIGHT -> Triple(0.25, 0.50, 0.25)
                MainGoal.BOOST_ENERGY -> Triple(0.20, 0.55, 0.25)
                MainGoal.IMPROVE_NUTRITION -> Triple(0.25, 0.45, 0.30)
                MainGoal.GAIN_WEIGHT -> Triple(0.25, 0.55, 0.20)
            }
        }
        val protein = ratios.sumOf { it.first } / ratios.size
        val carbs = ratios.sumOf { it.second } / ratios.size
        val fat = ratios.sumOf { it.third } / ratios.size
        return Triple(protein, carbs, fat)
    }
}