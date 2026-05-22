package com.muhammad.nutribot.data.reminders

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.muhammad.nutribot.domain.repository.reminder.MealReminderScheduler
import com.muhammad.nutribot.utils.Constants.BREAKFAST_REMINDER
import com.muhammad.nutribot.utils.Constants.DINNER_REMINDER
import com.muhammad.nutribot.utils.Constants.LUNCH_REMINDER
import com.muhammad.nutribot.utils.Constants.MEAL_TYPE
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.util.concurrent.TimeUnit
import kotlin.time.Clock

class MealReminderSchedulerImp(
    private val context: Context,
) : MealReminderScheduler {
    override fun scheduleReminders() {
        scheduleMealReminder(
            context = context,
            uniqueWorkName = BREAKFAST_REMINDER,
            mealType = BREAKFAST_REMINDER,
            targetHour = 8
        )
        scheduleMealReminder(
            context = context,
            uniqueWorkName = LUNCH_REMINDER,
            mealType = LUNCH_REMINDER,
            targetHour = 13
        )
        scheduleMealReminder(
            context = context,
            uniqueWorkName = DINNER_REMINDER,
            mealType = DINNER_REMINDER,
            targetHour = 20
        )
    }

    override fun cancelReminders() {
        WorkManager.getInstance(context).cancelUniqueWork(BREAKFAST_REMINDER)
        WorkManager.getInstance(context).cancelUniqueWork(LUNCH_REMINDER)
        WorkManager.getInstance(context).cancelUniqueWork(DINNER_REMINDER)
    }

    private fun scheduleMealReminder(
        context: Context,
        uniqueWorkName: String,
        mealType: String,
        targetHour: Int,
    ) {
        val now = Clock.System.now()
        val timeZone = TimeZone.currentSystemDefault()
        val currentDateTime = now.toLocalDateTime(timeZone)
        var targetDateTime = currentDateTime.date.atTime(targetHour, 0)
        if(targetDateTime <= currentDateTime){
            targetDateTime = currentDateTime.date.plus(1, DateTimeUnit.DAY).atTime(targetHour, 0)
        }
        val delayMillis = targetDateTime.toInstant(timeZone).toEpochMilliseconds() - now.toEpochMilliseconds()
        val data = Data.Builder().putString(MEAL_TYPE, mealType).build()
        val request =
            PeriodicWorkRequestBuilder<MealReminderWorker>(24, TimeUnit.HOURS).setInitialDelay(
                delayMillis,
                TimeUnit.MILLISECONDS
            ).setInputData(data).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            uniqueWorkName,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            request
        )
    }
}