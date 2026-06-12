package com.muhammad.nutribot.presentation.screens.credits

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.muhammad.nutribot.R
import com.muhammad.nutribot.utils.circleBubbles

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CreditScreen(navController: NavHostController) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(text = stringResource(R.string.credits))
            }, navigationIcon = {
                IconButton(onClick = {
                    navController.navigateUp()
                }, shapes = IconButtonDefaults.shapes()) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left),
                        contentDescription = null
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .circleBubbles()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(60.dp, Alignment.CenterVertically)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier.size(45.dp)
            )
            CreditItem(
                title = stringResource(R.string.idea_and_execution),
                label = stringResource(R.string.app_cretor)
            )
            CreditItem(
                title = stringResource(R.string.android),
                label = stringResource(R.string.app_cretor)
            )
            CreditItem(
                title = stringResource(R.string.design),
                label = stringResource(R.string.app_cretor)
            )
            CreditItem(
                title = stringResource(R.string.company),
                label = stringResource(R.string.company_name)
            )
        }
    }
}

@Composable
fun CreditItem(title: String, label: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.surface,
                textAlign = TextAlign.Center
            )
        )
        Text(text = label, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
    }
}