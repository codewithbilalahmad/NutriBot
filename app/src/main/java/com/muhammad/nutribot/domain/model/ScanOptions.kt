package com.muhammad.nutribot.domain.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.muhammad.nutribot.R

@Immutable
enum class ScanOptions(@get:StringRes val  label : Int, val icon : Int) {
    MEAL(label = R.string.scan_meal, icon = R.drawable.ic_meal),
    BARCODE(label = R.string.scan_barcode, icon = R.drawable.ic_barcode),
}