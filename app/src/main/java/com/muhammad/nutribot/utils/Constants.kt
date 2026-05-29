package com.muhammad.nutribot.utils


object Constants {
    const val DATABASE_NAME = "NutriBotDatabase.db"
    const val DATA_STORE_FILE_NAME = "app_settings.preferences_pb"
    const val USER_PROFILE_PREF_KEY = "user_profile"
    const val NUTRITION_CALCULATION_PREF_KEY = "nutrition_calculation"
    const val IS_USER_LOGGED_IN_PREF_KEY = "is_user_logged_in"
    const val IS_REMINDER_ENABLE_PREF_KEY = "is_reminder_enable"
    const val MEAL_TYPE = "meal_type"
    const val MEAL_REMINDER_NAME = "Meal Reminder"
    const val BREAKFAST_REMINDER = "breakfast_reminder"
    const val LUNCH_REMINDER = "launch_reminder"
    const val DINNER_REMINDER = "dinner_reminder"
    const val MEAL_REMINDER_CHANNEL = "meal_reminder_channel"
    const val GEMINI_API_KEY = BuildConfig.API_KEY
    const val GEMINI_MODEL_NAME = "gemini-2.5-flash-lite"
    const val BEST_STREAK_PREF_KEY = "best_streak"
}