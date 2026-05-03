package com.muhammad.nutribot

import android.app.Application
import com.muhammad.nutribot.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class NutriBotApplication : Application() {
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
    }
}