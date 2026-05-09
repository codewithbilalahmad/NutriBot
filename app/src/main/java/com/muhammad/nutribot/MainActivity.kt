package com.muhammad.nutribot

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.muhammad.nutribot.main.MainViewModel
import com.muhammad.nutribot.presentation.navigation.AppNavigation
import com.muhammad.nutribot.presentation.theme.NutriBotTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    val viewModel: MainViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.value.isCheckingLogin
            }
        }
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
        )
        setContent {
            NutriBotTheme {
                val state by viewModel.state.collectAsStateWithLifecycle()
                val navController = rememberNavController()
                AppNavigation(
                    navController = navController,
                    isUserLoggedIn = state.isUserLoggedIn == true
                )
            }
        }
    }
}