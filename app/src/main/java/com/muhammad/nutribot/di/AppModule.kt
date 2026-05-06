package com.muhammad.nutribot.di

import com.muhammad.nutribot.data.nutrition.NutritionCalculationRepositoryImp
import com.muhammad.nutribot.domain.repository.NutritionCalculationRepository
import com.muhammad.nutribot.presentation.screens.nurition_setup.NutritionSetupViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    singleOf(::NutritionCalculationRepositoryImp).bind<NutritionCalculationRepository>()
    viewModelOf(::NutritionSetupViewModel)
}