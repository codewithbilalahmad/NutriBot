package com.muhammad.nutribot.domain.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.muhammad.nutribot.R

@Immutable
enum class Gender(@get:StringRes val value: Int, val icon : Int) {
    MALE(value = R.string.male, icon = R.drawable.ic_male),
    FEMALE(value = R.string.female, icon = R.drawable.ic_female),
}