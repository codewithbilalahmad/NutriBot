package com.muhammad.nutribot.domain.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.muhammad.nutribot.R

@Immutable
enum class Gender(
    @get:StringRes val value: Int,
    val icon: Int,
    val emoji: String
) {
    MALE(
        value = R.string.male,
        icon = R.drawable.ic_male,
        emoji = "👨"
    ),
    FEMALE(
        value = R.string.female,
        icon = R.drawable.ic_female,
        emoji = "👩"
    ),
    PREFER_NOT_TO_SAY(
        value = R.string.prefer_not_to_say,
        icon = R.drawable.ic_question,
        emoji = "⁉"
    )
}