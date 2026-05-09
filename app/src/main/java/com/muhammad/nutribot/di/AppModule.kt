package com.muhammad.nutribot.di

import android.content.Context
import androidx.room.Room
import com.muhammad.nutribot.NutriBotApplication
import com.muhammad.nutribot.data.local.NutriBotDatabase
import com.muhammad.nutribot.data.repository.food.FoodRepositoryImp
import com.muhammad.nutribot.data.repository.ingredient.IngredientRepositoryImp
import com.muhammad.nutribot.data.repository.nutrition_calculation.NutritionCalculationRepositoryImp
import com.muhammad.nutribot.data.repository.settings.SettingRepositoryImp
import com.muhammad.nutribot.domain.repository.food.FoodRepository
import com.muhammad.nutribot.domain.repository.ingredient.IngredientRepository
import com.muhammad.nutribot.domain.repository.nutrition_calculation.NutritionCalculationRepository
import com.muhammad.nutribot.domain.repository.settings.SettingRepository
import com.muhammad.nutribot.main.MainViewModel
import com.muhammad.nutribot.presentation.screens.diary.DiaryViewModel
import com.muhammad.nutribot.presentation.screens.nurition_setup.NutritionSetupViewModel
import com.muhammad.nutribot.utils.Constants.DATABASE_NAME
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { NutriBotApplication.INSTANCE }
    single {
        Room.databaseBuilder<NutriBotDatabase>(get<Context>(), DATABASE_NAME)
            .setQueryCoroutineContext(Dispatchers.IO).build()
    }
    single {
        get<NutriBotDatabase>().foodDao()
    }
    single {
        get<NutriBotDatabase>().ingredientDao()
    }
    singleOf(::FoodRepositoryImp).bind<FoodRepository>()
    singleOf(::IngredientRepositoryImp).bind<IngredientRepository>()
    singleOf(::SettingRepositoryImp).bind<SettingRepository>()
    singleOf(::NutritionCalculationRepositoryImp).bind<NutritionCalculationRepository>()
    viewModelOf(::MainViewModel)
    viewModelOf(::NutritionSetupViewModel)
    viewModelOf(::DiaryViewModel)
}