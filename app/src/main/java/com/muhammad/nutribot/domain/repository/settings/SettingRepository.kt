package com.muhammad.nutribot.domain.repository.settings

import com.muhammad.nutribot.domain.model.NutritionCalculation
import com.muhammad.nutribot.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    suspend fun saveUserProfile(userProfile: UserProfile)
    suspend fun saveEnableReminder(enable : Boolean)
    suspend fun saveNutritionCalculation(nutritionCalculation: NutritionCalculation)
    suspend fun saveIsUserLoggedIn(loggedIn : Boolean)
    suspend fun saveBestStreak(streak : Int)
    fun observeUserProfile() : Flow<UserProfile?>
    fun observeBestStreak() : Flow<Int>
    fun observeNutritionCalculation() : Flow<NutritionCalculation?>
    fun observeIsUserLoggedIn() : Flow<Boolean>
    fun observeEnableReminder() : Flow<Boolean>
}