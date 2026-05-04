package com.muhammad.nutribot.presentation.screens.nurition_setup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.R
import com.muhammad.nutribot.presentation.components.measurement_picker.MeasurementPicker

@Composable
fun HeightStepSection(
    modifier: Modifier = Modifier,
    selectedHeightCm: Int,
    onHeightSelected: (Int) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "$selectedHeightCm",
                style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = stringResource(R.string.cm),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        MeasurementPicker(
            minMeasurement = 120,
            maxMeasurement = 220,
            initialMeasurement = 150,
            modifier = Modifier.fillMaxWidth(), onMeasurementChange = onHeightSelected
        )
    }
}