package com.muhammad.nutribot.presentation.components.lottie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun AppLottieAnimation(modifier: Modifier = Modifier, lottieId: Int) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(lottieId)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition, iterations = LottieConstants.IterateForever
    )
    LottieAnimation(composition = composition, progress = { progress }, modifier = modifier)
}