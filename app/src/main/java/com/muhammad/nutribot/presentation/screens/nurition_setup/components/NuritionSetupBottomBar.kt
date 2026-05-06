package com.muhammad.nutribot.presentation.screens.nurition_setup.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import com.muhammad.nutribot.R
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.presentation.components.button.PrimaryButton

@Composable
fun NuritionSetupBottomBar(
    modifier: Modifier = Modifier,
    onNextStepClick: () -> Unit,
    @StringRes label: Int = R.string.continue_to,
    isContinueEnable: Boolean,
) {
    Column(
        modifier = modifier
            .navigationBarsPadding()
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.5.dp,
            color = MaterialTheme.colorScheme.surfaceVariant
        )
        PrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 12.dp),
            onClick = onNextStepClick, enabled = isContinueEnable,
            contentPadding = PaddingValues(vertical = 16.dp),
            text = stringResource(label)
        )
    }
}