package com.muhammad.nutribot.presentation.components.snakbar

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.muhammad.nutribot.domain.model.GradientSnackbarVisuals

@Composable
fun GradientSnackbarHost(
    snackbarHostState: SnackbarHostState,
) {

    SnackbarHost(
        hostState = snackbarHostState,
    ) { snackbarData ->

        val visuals = snackbarData.visuals

        if (visuals is GradientSnackbarVisuals) {
            GradientSnackbar(
                snackbarVisuals = visuals,
                onDismiss = {
                    snackbarData.dismiss()
                }
            )
        } else {
            Snackbar(snackbarData = snackbarData)
        }
    }
}