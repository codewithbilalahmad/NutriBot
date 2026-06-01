package com.muhammad.nutribot.di

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import com.muhammad.nutribot.NutriBotApplication
import com.muhammad.nutribot.data.local.NutriBotDatabase
import com.muhammad.nutribot.data.reminders.MealReminderSchedulerImp
import com.muhammad.nutribot.data.remote.network.SearchFoodNetworkImp
import com.muhammad.nutribot.data.remote.network.network.HttpClientFactory
import com.muhammad.nutribot.data.remote.repository.SearchFoodRespositoryImp
import com.muhammad.nutribot.data.repository.camera.CameraControllerImp
import com.muhammad.nutribot.data.repository.connection.AndroidConnectivityObserver
import com.muhammad.nutribot.data.repository.food.FoodRepositoryImp
import com.muhammad.nutribot.data.repository.food_history.FoodHistoryRespositoryImp
import com.muhammad.nutribot.data.repository.ingredient.IngredientRepositoryImp
import com.muhammad.nutribot.data.repository.nutrition_calculation.NutritionCalculationRepositoryImp
import com.muhammad.nutribot.data.repository.settings.SettingRepositoryImp
import com.muhammad.nutribot.domain.network.SearchFoodNetwork
import com.muhammad.nutribot.domain.repository.camera.CameraController
import com.muhammad.nutribot.domain.repository.connection.ConnectivityObserver
import com.muhammad.nutribot.domain.repository.food.FoodRepository
import com.muhammad.nutribot.domain.repository.food_history.FoodHistoryRespository
import com.muhammad.nutribot.domain.repository.ingredient.IngredientRepository
import com.muhammad.nutribot.domain.repository.nutrition_calculation.NutritionCalculationRepository
import com.muhammad.nutribot.domain.repository.reminder.MealReminderScheduler
import com.muhammad.nutribot.domain.repository.search_food.SearchFoodRespository
import com.muhammad.nutribot.domain.repository.settings.SettingRepository
import com.muhammad.nutribot.main.MainViewModel
import com.muhammad.nutribot.presentation.screens.diary.DiaryViewModel
import com.muhammad.nutribot.presentation.screens.favourite_meals.FavouriteMealsViewModel
import com.muhammad.nutribot.presentation.screens.meal_details.MealDetailViewModel
import com.muhammad.nutribot.presentation.screens.nurition_setup.NutritionSetupViewModel
import com.muhammad.nutribot.presentation.screens.scan_meal.ScanMealViewModel
import com.muhammad.nutribot.presentation.screens.search_meal.SearchMealViewModel
import com.muhammad.nutribot.presentation.screens.setting.SettingViewModel
import com.muhammad.nutribot.presentation.screens.streak.StreakViewModel
import com.muhammad.nutribot.presentation.screens.streak_progress.StreakProgressViewModel
import com.muhammad.nutribot.utils.Constants.DATABASE_NAME
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { NutriBotApplication.INSTANCE }
    single { HttpClientFactory.createClient() }
    singleOf(::AndroidConnectivityObserver).bind<ConnectivityObserver>()
    singleOf(::MealReminderSchedulerImp).bind<MealReminderScheduler>()
    singleOf(::CameraControllerImp).bind<CameraController>()
    single {
        Room.databaseBuilder<NutriBotDatabase>(get<Context>(), DATABASE_NAME)
            .setQueryCoroutineContext(Dispatchers.IO).fallbackToDestructiveMigration(dropAllTables = true).build()
    }
    single {
        get<NutriBotDatabase>().foodDao()
    }
    single {
        get<NutriBotDatabase>().ingredientDao()
    }
    single {
        get<NutriBotDatabase>().historyFoodDao()
    }
    singleOf(::SearchFoodNetworkImp).bind<SearchFoodNetwork>()
    singleOf(::FoodRepositoryImp).bind<FoodRepository>()
    singleOf(::FoodHistoryRespositoryImp).bind<FoodHistoryRespository>()
    singleOf(::IngredientRepositoryImp).bind<IngredientRepository>()
    singleOf(::SettingRepositoryImp).bind<SettingRepository>()
    singleOf(::NutritionCalculationRepositoryImp).bind<NutritionCalculationRepository>()
    singleOf(::SearchFoodRespositoryImp).bind<SearchFoodRespository>()
    viewModelOf(::MainViewModel)
    viewModelOf(::NutritionSetupViewModel)
    viewModelOf(::DiaryViewModel)
    viewModelOf(::SettingViewModel)
    viewModelOf(::ScanMealViewModel)
    viewModelOf(::MealDetailViewModel)
    viewModelOf(::StreakViewModel)
    viewModelOf(::StreakProgressViewModel)
    viewModelOf(::FavouriteMealsViewModel)
    viewModelOf(::SearchMealViewModel)
}