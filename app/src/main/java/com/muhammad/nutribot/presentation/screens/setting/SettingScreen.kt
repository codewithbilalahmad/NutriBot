package com.muhammad.nutribot.presentation.screens.setting

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import com.muhammad.nutribot.R
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.muhammad.nutribot.presentation.navigation.Destination
import com.muhammad.nutribot.presentation.screens.setting.components.SettingHeader

@Composable
fun SettingScreen(navHostController: NavHostController) {
    val layoutDirection = LocalLayoutDirection.current
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(text = stringResource(R.string.settings))
            },
            navigationIcon = {
                IconButton(onClick = {
                    navHostController.navigateUp()
                }, shapes = IconButtonDefaults.shapes()) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left),
                        contentDescription = null
                    )
                }
            }
        )
    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    paddingValues = PaddingValues(
                        start = paddingValues.calculateStartPadding(layoutDirection),
                        end = paddingValues.calculateStartPadding(layoutDirection),
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding()
                    )
                )
        ) {
            item("topbar_divider") {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            }
            item("profile_header"){
                SettingHeader(label = R.string.profile)
            }
        }
    }
}