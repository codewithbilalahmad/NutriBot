package com.muhammad.nutribot.utils

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.plus

fun LocalDate.startOfDayMillis(): Long {
    return atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
}

fun LocalDate.endOdDayMillis(): Long {
    return plus(1, DateTimeUnit.DAY).atStartOfDayIn(TimeZone.currentSystemDefault())
        .toEpochMilliseconds() - 1
}

fun getWeekDatesFrom(date: LocalDate): List<LocalDate> {
    val startOfWeek = date.minus(date.dayOfWeek.ordinal, DateTimeUnit.DAY)
    return (0..6).map { day ->
        startOfWeek.plus(day, DateTimeUnit.DAY)
    }
}