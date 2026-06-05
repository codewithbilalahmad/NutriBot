package com.muhammad.nutribot.presentation.screens.scan_meal.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.R
import com.muhammad.nutribot.domain.model.ScanOption
import com.muhammad.nutribot.utils.rippleClickable

@Composable
fun ScanMealBottomBar(
    modifier: Modifier = Modifier,
    isFlashOn: Boolean,
    enabled: Boolean,
    scanOption: ScanOption,
    isCaptureButtonEnabled: Boolean,
    selectedScanOption: ScanOption,
    onSelectScanOption: (ScanOption) -> Unit,
    onCaptureMealPhoto: () -> Unit,
    onToggleFlash: () -> Unit,
    onPickMealGalleryImage: () -> Unit,
    onToggleBarcodeNumberSection : () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 24.dp, top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ScanOptionsSection(
            selectedOption = selectedScanOption,
            onSelectScanOption = onSelectScanOption
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(
                onClick = onPickMealGalleryImage,
                modifier = Modifier.size(IconButtonDefaults.mediumContainerSize()),
                shapes = IconButtonDefaults.shapes(),
                enabled = enabled,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.background.copy(0.4f),
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_gallery),
                    contentDescription = null
                )
            }
            AnimatedContent(targetState = scanOption, transitionSpec = {
                fadeIn() togetherWith fadeOut()
            }, modifier = Modifier.weight(1f)) { option ->
                when (option) {
                    ScanOption.MEAL -> {
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            ScanMealButton(
                                onClick = onCaptureMealPhoto,
                                enabled = isCaptureButtonEnabled
                            )
                        }
                    }

                    ScanOption.BARCODE -> {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.background.copy(0.4f))
                                .padding(vertical = 16.dp).rippleClickable(onClick = onToggleBarcodeNumberSection), contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.enter_barcode_number),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
            IconButton(
                onClick = onToggleFlash,
                modifier = Modifier.size(IconButtonDefaults.mediumContainerSize()),
                enabled = enabled,
                shapes = IconButtonDefaults.shapes(),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.background.copy(0.4f),
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