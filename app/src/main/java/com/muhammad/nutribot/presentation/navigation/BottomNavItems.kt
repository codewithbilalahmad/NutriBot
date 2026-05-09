package com.muhammad.nutribot.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.muhammad.nutribot.R

@Immutable
enum class BottomNavItem(
    val route: Destination,
    @get:StringRes val title: Int,
    val icon: Int
) {
    Diary(
        route = Destination.DiaryScreen,
        title = R.string.diary,
        icon = R.drawable.ic_diary
    ),
    Progress(
        route = Destination.ProgressScreen,
        title = R.string.progress,
        icon = R.drawable.ic_progress
    )
}