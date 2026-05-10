package com.muhammad.nutribot.presentation.components.wheel_picker

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import kotlin.math.abs

@Composable
fun WheelPickerHorizontal(
    modifier: Modifier = Modifier,
    range: IntRange,
    initialValue: Int,
    visibleItemsCount: Int = 5,
    onItemSelected: (Int) -> Unit,
) {
    val density = LocalDensity.current

    val actualItems = remember(range) {
        range.toList()
    }

    val paddingItemCount = visibleItemsCount / 2

    val items = remember(actualItems) {
        List(paddingItemCount) { null } +
                actualItems +
                List(paddingItemCount) { null }
    }

    val initialIndex = remember(initialValue) {
        actualItems.indexOf(initialValue) + paddingItemCount
    }

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = initialIndex - paddingItemCount
    )

    val flingBehavior = rememberSnapFlingBehavior(listState)

    val centerIndex by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex + paddingItemCount
        }
    }

    val offset by remember {
        derivedStateOf {
            listState.firstVisibleItemScrollOffset.toFloat()
        }
    }

    LaunchedEffect(centerIndex) {
        items.getOrNull(centerIndex)?.let {
            onItemSelected(it)
        }
    }
    BoxWithConstraints(modifier = modifier) {
        val itemWidth = maxWidth / visibleItemsCount
        val itemWidthPx = with(density) { itemWidth.toPx() }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            state = listState,
            flingBehavior = flingBehavior,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            itemsIndexed(
                items = items,
                key = { index, _ -> index }
            ) { index, item ->

                if (item == null) {

                    Spacer(
                        modifier = Modifier.width(itemWidth)
                    )

                } else {

                    val distanceFromCenter =
                        (index - centerIndex) - (offset / itemWidthPx)

                    val normalizedDistance =
                        abs(distanceFromCenter).coerceAtMost(1f)

                    val alpha = 1f - (normalizedDistance * 0.5f)

                    val scale = 1f - (normalizedDistance * 0.2f)

                    val rotation = distanceFromCenter * 20f

                    val isSelected = abs(distanceFromCenter) < 0.5f

                    Text(
                        text = item.toString(),
                        modifier = Modifier
                            .width(itemWidth)
                            .graphicsLayer {
                                this.alpha = alpha
                                scaleX = scale
                                scaleY = scale

                                rotationY = if (distanceFromCenter < 0) rotation else -rotation

                                transformOrigin = TransformOrigin.Center

                                cameraDistance = 12f * density.density * 100
                            },
                        style = MaterialTheme.typography.displaySmall,
                        color = if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }, textAlign = when {
                            isSelected -> TextAlign.Center
                            distanceFromCenter < 0 -> TextAlign.Left
                            else -> TextAlign.Right
                        }, fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.horizontalGradient(
                        colorStops = arrayOf(
                            0.2f to MaterialTheme.colorScheme.background.copy(0.6f),
                            0.3f to Color.Transparent,
                            0.7f to Color.Transparent,
                            1f to MaterialTheme.colorScheme.background.copy(0.6f)
                        )
                    )
                )
        )
    }
}