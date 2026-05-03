package com.muhammad.nutribot.di

import com.muhammad.nutribot.presentation.screens.nurition_setup.NuritionSetupViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::NuritionSetupViewModel)
}