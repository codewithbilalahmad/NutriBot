package com.muhammad.nutribot.data.reminders

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.muhammad.nutribot.R
import com.muhammad.nutribot.utils.Constants.MEAL_REMINDER_CHANNEL
import com.muhammad.nutribot.utils.Constants.MEAL_REMINDER_NAME
import com.muhammad.nutribot.utils.Constants.MEAL_TYPE

class MealReminderWorker(
    private val context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val mealType = inputData.getString(MEAL_TYPE)
        createNotificationChannel()
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return Result.failure()
        }
        val contentTexts = when (mealType) {

            "Breakfast" -> listOf(
                "Start your day with a healthy breakfast 🍳",
                "Good morning! Don’t forget to track your breakfast ☀️",
                "Fuel your morning with a nutritious meal 🥗",
                "Breakfast time! Log your calories and stay on track 📊",
                "A healthy breakfast sets the tone for the day 🌞",
                "Time for breakfast! Your body needs energy 🍞",
                "Track your breakfast and hit your nutrition goals 🎯",
                "Morning meals matter — log your breakfast now 🥣",
                "Stay consistent with your breakfast calories 💪",
                "Healthy mornings begin with smart eating 🍓"
            )

            "Lunch" -> listOf(
                "Lunch time! Refuel your energy 🍛",
                "Don’t skip lunch — track your calories 🍱",
                "Keep your nutrition balanced this afternoon 🥗",
                "Time to enjoy your lunch and stay healthy 🌮",
                "Track your lunch meal and stay consistent 📈",
                "A healthy lunch keeps you productive ⚡",
                "Lunch reminder! Eat well and feel better 🍜",
                "Stay energized with a nutritious lunch 🥪",
                "Midday meal check-in 🍽️",
                "Keep crushing your calorie goals this afternoon 💪"
            )

            "Dinner" -> listOf(
                "Dinner time! Finish your day healthy 🌙",
                "Track your dinner calories before bedtime 🍲",
                "Enjoy a balanced dinner tonight 🥘",
                "Healthy dinners lead to better mornings ✨",
                "Don’t forget to log your dinner 🍛",
                "End your day with smart nutrition 🌮",
                "Dinner reminder! Stay consistent with your goals 📊",
                "A light and healthy dinner is always a win 🥗",
                "Track tonight’s meal and stay on track 🎯",
                "Good evening! Time for dinner 🍽️"
            )

            else -> listOf(
                "Don’t forget to track your meal 🍽️"
            )
        }
        val notification = NotificationCompat.Builder(context, MEAL_REMINDER_CHANNEL)
            .setSmallIcon(R.drawable.ic_launcher_foreground).setContentTitle(
                "$mealType Reminder"
            ).setContentText(contentTexts.random()).setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true).build()
        NotificationManagerCompat.from(context).notify(mealType.hashCode(), notification)
        return Result.success()
    }
    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =    NotificationChannel(
                MEAL_REMINDER_CHANNEL,
                MEAL_REMINDER_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            NotificationManagerCompat.from(context).createNotificationChannel(channel)
        }
    }
}