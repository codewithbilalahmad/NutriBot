package com.muhammad.nutribot

import android.app.Application
import com.muhammad.nutribot.di.appModule
import com.muhammad.nutribot.domain.repository.reminder.MealReminderScheduler
import com.muhammad.nutribot.domain.repository.settings.SettingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class NutriBotApplication : Application() {
    private val settingRepository : SettingRepository by inject()
    private val reminderScheduler : MealReminderScheduler by inject()
    companion object{
        lateinit var INSTANCE : NutriBotApplication
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        startKoin {
            androidContext(this@NutriBotApplication)
            androidLogger()
            modules(appModule)
        }
        CoroutineScope(Dispatchers.IO).launch{
            settingRepository.observeEnableReminder().collectLatest { enabled ->
                if (enabled) {
                    reminderScheduler.scheduleReminders()
                } else {
                    reminderScheduler.cancelReminders()
                }
            }
        }
    }
}