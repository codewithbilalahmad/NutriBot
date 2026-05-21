package com.muhammad.nutribot.domain.repository.reminder

interface MealReminderScheduler{
    fun scheduleReminders()
    fun cancelReminders()
}