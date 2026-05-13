package com.muhammad.nutribot.presentation.screens.scan_meal.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.R
import com.muhammad.nutribot.domain.model.ScanOption

@Composable
fun ScanMealBottomBar(
    modifier: Modifier = Modifier,
    isFlashOn: Boolean,
    enabled: Boolean,
    isCaptureButtonEnabled : Boolean,
    selectedScanOption : ScanOption,
    onSelectScanOption : (ScanOption) -> Unit,
    onCaptureMealPhoto: () -> Unit,
    onToggleFlash: () -> Unit,
    onPickMealGalleryImage: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 24.dp,top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ScanOptionsSection(selectedOption = selectedScanOption, onSelectScanOption = onSelectScanOption)
        Row(
            modifier = modifier.fillMaxWidth().padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onPickMealGalleryImage,
                modifier = Modifier.size(IconButtonDefaults.mediumContainerSize()),
                shapes = IconButtonDefaults.shapes(),
                enabled = enabled,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.background.copy(0.2f),
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_gallery),
                    contentDescription = null
                )
            }
            ScanMealButton(onClick = onCaptureMealPhoto, enabled = isCaptureButtonEnabled)
            IconButton(
                onClick = onToggleFlash,
                modifier = Modifier.size(IconButtonDefaults.mediumContainerSize()),
                enabled = enabled,
                shapes = IconButtonDefaults.shapes(),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.background.copy(0.2f),
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            ) {
                val icon = if (isFlashOn) R.drawable.ic_flash else R.drawable.ic_flash_off
                Icon(
                    imageVector = ImageVector.vectorResource(icon),
                    contentDescription = null
                )
            }
        }
    }
}