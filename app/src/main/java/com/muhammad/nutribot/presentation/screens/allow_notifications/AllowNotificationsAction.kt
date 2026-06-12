package com.muhammad.nutribot.presentation.screens.allow_notifications

sealed interface AllowNotificationsAction{
    data object OnToggleNotificationPermissionDeniedDialog : AllowNotificationsAction
    data object OnStartNutritionPlan : AllowNotificationsAction
}