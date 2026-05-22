package com.muhammad.nutribot.data.reminders

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.muhammad.nutribot.domain.repository.reminder.MealReminderScheduler
import com.muhammad.nutribot.domain.repository.settings.SettingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BootReceiver : BroadcastReceiver(), KoinComponent{
    private val settingRepository : SettingRepository by inject()
    private val reminderScheduler : MealReminderScheduler by inject()
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            CoroutineScope(Dispatchers.IO).launch{
                val enabled = settingRepository.observeEnableReminder().first()
                if (enabled) {
                    reminderScheduler.scheduleReminders()
                } else {
                    reminderScheduler.cancelReminders()
                }
            }
        }
    }
}