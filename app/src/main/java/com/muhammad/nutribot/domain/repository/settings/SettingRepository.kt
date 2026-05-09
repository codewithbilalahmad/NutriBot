package com.muhammad.nutribot.domain.repository.settings

import com.muhammad.nutribot.domain.model.NutritionCalculation
import com.muhammad.nutribot.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    suspend fun saveUserProfile(userProfile: UserProfile)
    suspend fun saveNutritionCalculation(nutritionCalculation: NutritionCalculation)
    suspend fun saveIsUserLoggedIn(loggedIn : Boolean)
    fun observeUserProfile() : Flow<UserProfile?>
    fun observeNutritionCalculation() : Flow<NutritionCalculation?>
    fun observeIsUserLoggedIn() : Flow<Boolean>
}