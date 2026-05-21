package com.muhammad.nutribot.data.reminders

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.muhammad.nutribot.R
import com.muhammad.nutribot.domain.repository.reminder.MealReminderScheduler
import com.muhammad.nutribot.utils.Constants.BREAKFAST_REMINDER
import com.muhammad.nutribot.utils.Constants.DINNER_REMINDER
import com.muhammad.nutribot.utils.Constants.LUNCH_REMINDER
import com.muhammad.nutribot.utils.Constants.MEAL_TYPE
import kotlinx.datetime.TimeZone
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
            mealType = context.getString(R.string.breakfast),
            targetHour = 8
        )
        scheduleMealReminder(
            context = context,
            uniqueWorkName = LUNCH_REMINDER,
            mealType = context.getString(R.string.launch),
            targetHour = 13
        )
        scheduleMealReminder(
            context = context,
            uniqueWorkName = DINNER_REMINDER,
            mealType = context.getString(R.string.dinner),
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
        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val currentHour = currentDateTime.hour
        val currentMinute = currentDateTime.minute
        var delayHour = targetHour - currentHour
        if (delayHour < 0) {
            delayHour += 24
        }
        val delayMillis =
            TimeUnit.HOURS.toMillis(delayHour.toLong()) - TimeUnit.MINUTES.toMillis(currentMinute.toLong())
        val data = Data.Builder().putString(MEAL_TYPE, mealType).build()
        val request =
            PeriodicWorkRequestBuilder<MealReminderWorker>(24, TimeUnit.HOURS).setInitialDelay(
                delayMillis,
                TimeUnit.MILLISECONDS
            ).setInputData(data).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            uniqueWorkName,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }
}