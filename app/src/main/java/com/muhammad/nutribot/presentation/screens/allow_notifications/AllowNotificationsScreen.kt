package com.muhammad.nutribot.presentation.screens.allow_notifications

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.muhammad.nutribot.R
import com.muhammad.nutribot.presentation.components.alert_dialog.AppAlertDialog
import com.muhammad.nutribot.presentation.components.button.PrimaryButton
import com.muhammad.nutribot.presentation.components.lottie.AppLottieAnimation
import com.muhammad.nutribot.utils.checkPermissionGranted
import com.muhammad.nutribot.utils.checkPermissionPermanentlyDenied
import com.muhammad.nutribot.utils.openPermissionSettings
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AllowNotificationsScreen(
    navHostController: NavHostController,
    viewModel: AllowNotificationsViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val activity = context as Activity
    val state by viewModel.state.collectAsStateWithLifecycle()
    val notificationPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            when {
                checkPermissionPermanentlyDenied(activity = activity, permission = Manifest.permission.POST_NOTIFICATIONS) ->{
                    viewModel.onAction(AllowNotificationsAction.OnToggleNotificationPermissionDeniedDialog)
                }
            }
        }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(onClick = {}, shapes = IconButtonDefaults.shapes()) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left),
                        contentDescription = null
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
        )
    }, bottomBar = {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextButton(onClick = {
                viewModel.onAction(AllowNotificationsAction.OnStartNutritionPlan)
            }, shapes = ButtonDefaults.shapes()) {
                Text(
                    text = stringResource(R.string.skip),
                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground)
                )
            }
            PrimaryButton(
                text = stringResource(R.string.allow_notifications),
                onClick = {
                    when{
                        checkPermissionGranted(context = context, permission = Manifest.permission.POST_NOTIFICATIONS) ->{
                            viewModel.onAction(AllowNotificationsAction.OnStartNutritionPlan)
                        }
                        else -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else{
                                viewModel.onAction(AllowNotificationsAction.OnStartNutritionPlan)
                            }
                        }
                    }
                },
                contentPadding = PaddingValues(vertical = 16.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.allow_notifications_title),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge.copy(
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.allow_notifications_desp),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.surface,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(Modifier.height(32.dp))
                AppLottieAnimation(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    lottieId = R.raw.notifications
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
                    .align(Alignment.BottomCenter)
            )
        }
    }
    if (state.showNotificationPermissionDeniedDialog) {
        AppAlertDialog(
            onDismiss = {
                viewModel.onAction(AllowNotificationsAction.OnToggleNotificationPermissionDeniedDialog)
            },
            title = stringResource(R.string.allow_reminders),
            message = stringResource(R.string.allow_reminders_desp),
            confirmText = stringResource(R.string.confirm),
            dismissText = stringResource(R.string.discard),
            onConfirmClick = {
                viewModel.onAction(AllowNotificationsAction.OnToggleNotificationPermissionDeniedDialog)
                openPermissionSettings(context)
            }, onDismissClick = {
                viewModel.onAction(AllowNotificationsAction.OnToggleNotificationPermissionDeniedDialog)
            }
        )
    }
}