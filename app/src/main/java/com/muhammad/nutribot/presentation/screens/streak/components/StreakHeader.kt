package com.muhammad.nutribot.presentation.screens.streak.components

import android.app.Activity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.muhammad.nutribot.R
import com.muhammad.nutribot.presentation.components.lottie.AppLottieAnimation

@Composable
fun StreakHeader(modifier: Modifier = Modifier, streak: Int, onBackClick: () -> Unit) {
    val context = LocalContext.current
    val activity = context as Activity
    val window = activity.window
    val density = LocalDensity.current
    val isDarkTheme = isSystemInDarkTheme()
    val streakDescription = remember(streak) {
        when (streak) {
            0 -> "Your journey is waiting to begin. Start today and build your very first streak!"

            1 -> "Great first step! Keep showing up every day to build strong momentum."

            in 2..3 -> "Nice progress so far! You're slowly building consistency and healthy habits."

            in 4..6 -> "Awesome work! Your dedication is starting to turn into a strong routine."

            in 7..13 -> "Impressive streak! Staying consistent like this is a huge achievement already."

            in 14..29 -> "Excellent dedication! Your discipline and daily effort are clearly paying off."

            in 30..59 -> "Amazing commitment! This habit is becoming a natural part of your lifestyle."

            else -> "Legendary consistency! You've built an incredible streak and you're truly unstoppable now."
        }
    }
    var streakAnimationHeight by remember { mutableStateOf(0.dp) }
    val infiniteTransition = rememberInfiniteTransition(label = "gradient_animation")

    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 6000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradient_offset"
    )
    DisposableEffect(Unit) {
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
            false
        onDispose {
            WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
                !isDarkTheme
        }
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        Color(0xFFFF6B6B),
                        Color(0xFFFF8E53),
                        Color(0xFF6B1A1A),
                        Color(0xFF2E0D0D)
                    ),
                    start = Offset(x = 0f, y = animatedOffset),
                    end = Offset(x = animatedOffset, y = 0f)
                )
            )
            .statusBarsPadding()
            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconButton(
            onClick = onBackClick,
            shapes = IconButtonDefaults.shapes(),
            colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left),
                contentDescription = null
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.onSizeChanged { size ->
                    streakAnimationHeight = with(density) { size.height.toDp() }
                }) {
                Text(
                    text = streak.toString(),
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 70.sp,
                        color = Color.White
                    )
                )
                Text(
                    text = stringResource(if (streak > 1) R.string.days_streak else R.string.day_streak),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.White
                    )
                )
            }
            AppLottieAnimation(
                modifier = Modifier.size(
                    width = 120.dp,
                    height = streakAnimationHeight
                ), lottieId = R.raw.fire_animation
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black.copy(0.5f))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_goal),
                contentDescription = null,
                modifier = Modifier.size(26.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Text(
                text = streakDescription,
                maxLines = 2, overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    fontSize = 13.sp, lineHeight = 18.sp
                )
            )
        }
    }
}