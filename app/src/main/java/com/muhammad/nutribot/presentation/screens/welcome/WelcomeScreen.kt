package com.muhammad.nutribot.presentation.screens.welcome

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.muhammad.nutribot.R
import com.muhammad.nutribot.presentation.navigation.Destination
import com.muhammad.nutribot.presentation.screens.welcome.components.GetStartedButton
import com.muhammad.nutribot.presentation.screens.welcome.components.WelcomeNutritionSection

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun WelcomeScreen(navController: NavHostController) {
    val layoutDirection = LocalLayoutDirection.current
    val bubbleColor = MaterialTheme.colorScheme.primaryContainer
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            WelcomeNutritionSection()
        }, bottomBar = {
            GetStartedButton(modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 16.dp), onClick = {
                navController.navigate(Destination.NutritionSetupScreen)
            })
        },
        containerColor = MaterialTheme.colorScheme.primary
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_calories),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.15f),
                modifier = Modifier
                    .size(400.dp)
                    .graphicsLayer {
                        rotationZ = -45f
                        translationY = paddingValues.calculateTopPadding().value
                    }
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        drawCircle(
                            color = bubbleColor,
                            radius = size.width * 0.9f,
                            center = Offset(x = size.width * 0.3f, y = size.height * 1f)
                        )
                        drawCircle(
                            color = bubbleColor,
                            radius = size.width * 0.92f,
                            style = Stroke(width = 10f, cap = StrokeCap.Round),
                            center = Offset(x = size.width * 0.3f, y = size.height * 1f)
                        )
                    }
                    .padding(paddingValues = PaddingValues(
                        start = paddingValues.calculateLeftPadding(layoutDirection),
                        end = paddingValues.calculateEndPadding(layoutDirection),
                        bottom = paddingValues.calculateBottomPadding(),
                    )),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Image(
                    painter = painterResource(R.drawable.welcome),
                    contentDescription = null,
                    modifier = Modifier.size(300.dp)
                )
                Text(
                    text = stringResource(R.string.welcome_title),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
                    )
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.welcome_desp),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}