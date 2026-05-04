package com.muhammad.nutribot.presentation.theme

import android.app.Activity
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


private val darkColorScheme = darkColorScheme(
    primary = Primary60,
    primaryContainer = Primary40,
    onPrimary = Color.White,
    onPrimaryContainer = Color.White,
    inversePrimary = Color(0x75FBCB2B),

    secondary = Secondary70,
    secondaryContainer = Secondary50,
    onSecondary = Secondary95,

    background = NeutralVariant10,
    surface = Color.Gray,
    surfaceVariant = Color(0xFF2C2C2E),
    onSurface = NeutralVariant99,
    onSurfaceVariant = NeutralVariant80,
    outline = NeutralVariant50,
    outlineVariant = NeutralVariant80,
    error = DarkError,
    errorContainer = DarkErrorContainer,
    onError = Color.White,
    onErrorContainer = Color.White
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NutriBotTheme(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val view = LocalView.current
    val activity = (context as Activity)
    val window = activity.window

    if (!view.isInEditMode) {
        SideEffect {
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = false
            controller.isAppearanceLightNavigationBars = false
        }
    }
    MaterialExpressiveTheme(
        colorScheme = darkColorScheme,
        typography = Typography,
        content = content,
        motionScheme = MotionScheme.expressive()
    )
}