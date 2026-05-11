package com.muhammad.nutribot.domain.model

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.muhammad.nutribot.presentation.components.snakbar.GradientSnackbar

@Composable
fun GradientSnackbarHost(snackbarHostState : SnackbarHostState) {
    SnackbarHost(hostState = snackbarHostState){snackbarData ->
        val visuals = snackbarData.visuals
        if(visuals is GradientSnackbarVisuals){
            GradientSnackbar(snackbarVisuals = GradientSnackbarVisuals(
                icon = visuals.icon,
                actionLabel = visuals.actionLabel,
                duration = visuals.duration, message = visuals.message, withDismissAction = visuals.withDismissAction
            ), onDismiss = {
                snackbarData.dismiss()
            })
        } else{
            Snackbar(snackbarData)
        }
    }
}