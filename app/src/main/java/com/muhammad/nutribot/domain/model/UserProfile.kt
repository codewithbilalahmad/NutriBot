package com.muhammad.nutribot.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class UserProfile(
    val username: String,
    val gender: Gender,
    val age: Int,
    val weightKg : Int,
    val heightCm : Int,
    val activityLevel: ActivityLevel,
    val mainGoal: List<MainGoal>
)
