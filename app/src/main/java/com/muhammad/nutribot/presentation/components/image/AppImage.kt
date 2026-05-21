package com.muhammad.nutribot.presentation.components.image

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.muhammad.nutribot.utils.decodeBitmap
import com.muhammad.nutribot.utils.loadingEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun AppImage(
    modifier: Modifier = Modifier,
    image: String,
    contentScale: ContentScale = ContentScale.Crop,
) {
    var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(image) {
        bitmap = withContext(Dispatchers.IO) {
            decodeBitmap(path = image)?.asImageBitmap()
        }
    }

    AnimatedContent(
        targetState = bitmap,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "AppImageTransition"
    ) { currentBitmap ->
        if (currentBitmap != null) {
            Image(
                bitmap = currentBitmap,
                contentDescription = null,
                modifier = modifier,
                contentScale = contentScale
            )
        } else {
            Box(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .loadingEffect()
            )
        }
    }
}