package com.muhammad.nutribot.presentation.components.wheel_picker

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.max

@Composable
fun WheelPicker(
    modifier: Modifier = Modifier,
    range: IntRange,
    initialValue: Int,
    selectedTextStyle : TextStyle = MaterialTheme.typography.titleMedium,
    unSelectedTextStyle : TextStyle = MaterialTheme.typography.titleMedium,
    @StringRes label : Int,
    visibleItemsCount: Int = 5,
    itemHeight: Dp = 40.dp,
    onItemSelected: (Int) -> Unit,
) {
    val density = LocalDensity.current
    val itemHeightPx = with(density) {
        itemHeight.toPx()
    }
    val actualItems = remember(range) { range.toList() }
    val paddingItemCount = visibleItemsCount / 2
    val items = remember(actualItems) {
        List(paddingItemCount) { null } + actualItems + List(paddingItemCount) { null }
    }
    val pickerHeight = itemHeight * visibleItemsCount
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialValue - 1)
    val firstVisibleItemIndex = remember {
        derivedStateOf { listState.firstVisibleItemIndex }
    }
    val flingBehavior = rememberSnapFlingBehavior(listState)
    val offset by remember {
        derivedStateOf { listState.firstVisibleItemScrollOffset.toFloat() }
    }
    val centerIndex = firstVisibleItemIndex.value + visibleItemsCount / 2
    LaunchedEffect(centerIndex) {
        val item = items.getOrNull(centerIndex)
        if (item != null) {
            onItemSelected(item)
        }
    }
    Box(modifier = modifier.height(pickerHeight), contentAlignment = Alignment.Center) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            flingBehavior = flingBehavior
        ) {
            itemsIndexed(items, key = { index, _ -> index }, contentType = { index, _ ->
                "item_${index}"
            }) { index, item ->
                if (item == null) {
                    Spacer(Modifier.height(itemHeight))
                } else {
                    val isSelected = index == centerIndex
                    val distanceFromCamera = (index - centerIndex) - (offset / itemHeightPx)
                    val alpha = max(0.5f, 1f - abs(distanceFromCamera) * 0.5f)
                    val angle = distanceFromCamera * 18f
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(itemHeight)
                            .graphicsLayer {
                                scaleY = 1f - alpha * 0.2f
                                rotationX = angle * if (index < centerIndex) 1f else -1f
                                this.alpha = alpha
                                transformOrigin = TransformOrigin.Center
                                cameraDistance = itemHeight.toPx() * density.density * 1000
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${item.toString()} ${stringResource(label)}",
                            style = unSelectedTextStyle.copy(
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.surface
                            )
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .height(itemHeight)
                .border(
                    width = 1.5.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(16.dp)
                )
                .background(MaterialTheme.colorScheme.surfaceContainer,RoundedCornerShape(16.dp))
                .padding(horizontal = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically){  }
            val item = items[centerIndex]
            Text(
                text = "${item.toString()} ${stringResource(label)}",
                style = selectedTextStyle.copy(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
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