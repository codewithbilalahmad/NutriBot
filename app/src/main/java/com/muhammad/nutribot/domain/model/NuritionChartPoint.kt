package com.muhammad.nutribot.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate

@Immutable
data class NuritionChartPoint(
    val date : LocalDate,
    val value : Int
)