package com.muhammad.nutribot.presentation.screens.setting

import com.muhammad.nutribot.domain.model.ActivityLevel
import com.muhammad.nutribot.domain.model.Gender
import com.muhammad.nutribot.domain.model.UserProfile

data class SettingState(
    val userProfile: UserProfile?=null,
    val reminderEnabled : Boolean = false,
    val showGenderSection : Boolean = false,
    val showAgeSection : Boolean = false,
    val showHeightAndHeightSection : Boolean = false,
    val showActivityLevelSection : Boolean = false
){
    val username = userProfile?.username.orEmpty()
    val gender = userProfile?.gender ?: Gender.MALE
    val age = userProfile?.age ?:0
    val height = userProfile?.heightCm ?:0
    val weight = userProfile?.weightKg ?:0
    val activityLevel = userProfile?.activityLevel ?: ActivityLevel.SEDENTARY
}