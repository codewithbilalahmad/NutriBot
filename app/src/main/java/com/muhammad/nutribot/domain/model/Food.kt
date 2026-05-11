package com.muhammad.nutribot.domain.model

import androidx.compose.runtime.Immutable
import kotlin.time.Instant

@Immutable
data class Food(
    val id : Long,
    val name : String,
    val calories : Int,
    val protein : Int,
    val fat : Int,
    val carbs : Int,
    val numberOfServings : Int,
    val eatenAt : Instant,
    val confidenceScore : Int,
    val ingredients: List<Ingredient>
)