package com.muhammad.nutribot.presentation.screens.scan_meal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.R
import com.muhammad.nutribot.presentation.components.textfield.AppTextField
import com.muhammad.nutribot.utils.rippleClickable

@Composable
fun BarcodeNumberSection(
    modifier: Modifier = Modifier,
    showBarcodeNumberSection: Boolean,
    barcodeNumber: TextFieldState,
    onAnalyzeBarcodeMeal: () -> Unit,
    onToggleBarcodeNumberSection: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(showBarcodeNumberSection) {
        if (showBarcodeNumberSection) {
            focusRequester.requestFocus()
        } else {
            focusRequester.freeFocus()
        }
    }
    if (showBarcodeNumberSection) {
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 16.dp)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.type_barcode_number),
                    style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground)
                )
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_cancel),
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(26.dp).rippleClickable(onClick = onToggleBarcodeNumberSection),
                    contentDescription = null
                )
            }
            AppTextField(
                state = barcodeNumber,
                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                leadingIcon = R.drawable.ic_barcode,
                onKeyboardAction = {
                    onAnalyzeBarcodeMeal()
                },
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
                lineLimits = TextFieldLineLimits.SingleLine,
                keyboardType = KeyboardType.Number,
                hint = R.string.barcode_hint,
                contentPadding = PaddingValues(16.dp)
            )
        }
    }
}