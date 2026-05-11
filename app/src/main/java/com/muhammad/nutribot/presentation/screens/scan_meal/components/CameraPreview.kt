package com.muhammad.nutribot.presentation.screens.scan_meal.components

import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CameraPreview(modifier: Modifier = Modifier, previewView: PreviewView) {
    AndroidView(factory = {
        previewView
    }, modifier = modifier)
}