package com.muhammad.nutribot.domain.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
data class NuritionSetupStep(
    @get:StringRes val label : Int
)