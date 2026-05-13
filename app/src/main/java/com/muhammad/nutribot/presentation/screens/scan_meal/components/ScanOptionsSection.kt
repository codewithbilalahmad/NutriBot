package com.muhammad.nutribot.presentation.screens.scan_meal.components

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.domain.model.ScanOption
import com.muhammad.nutribot.utils.rippleClickable

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ScanOptionsSection(
    modifier: Modifier = Modifier,
    selectedOption: ScanOption,
    onSelectScanOption: (ScanOption) -> Unit,
) {
    val list = ScanOption.entries
    val configuration = LocalConfiguration.current
    val listState = rememberLazyListState()
    LaunchedEffect(selectedOption) {
        val index = list.indexOf(selectedOption)
        listState.animateScrollToItem(index)
    }
    LazyRow(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.background.copy(0.2f))
            .padding(4.dp),
        state = listState,
        userScrollEnabled = false
    ) {
        items(list, key = { it.name }, contentType = { it.name }) { option ->
            val isSelected = option == selectedOption
            val containerColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.background.copy(0.5f)  else Color.Transparent,
                animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                label = "alpha"
            )
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(containerColor)
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .rippleClickable(onClick = {
                        onSelectScanOption(option)
                    }),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(option.icon),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(option.label),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}