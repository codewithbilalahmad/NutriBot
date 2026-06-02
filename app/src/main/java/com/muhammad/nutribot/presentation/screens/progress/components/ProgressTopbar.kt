package com.muhammad.nutribot.presentation.screens.progress.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.R
import com.muhammad.nutribot.presentation.theme.FlameYellow
import com.muhammad.nutribot.utils.rippleClickable

@Composable
fun ProgressTopbar(
    streak: Int,
    onStreakClick: () -> Unit,
    onSettingClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.progress))
        },
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .dropShadow(
                            shape = CircleShape, shadow = Shadow(
                                color = MaterialTheme.colorScheme.surfaceContainerLow,
                                spread = 2.dp,
                                radius = 2.dp
                            )
                        )
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.background)
                        .rippleClickable(onClick = onStreakClick)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        6.dp,
                        Alignment.CenterHorizontally
                    )
                ) {
                    val tint by animateColorAsState(
                        targetValue = if(streak > 0) FlameYellow else MaterialTheme.colorScheme.surface,
                        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                        label = "tint"
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_calories),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = tint
                    )
                    Text(
                        text = "$streak",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
                IconButton(onClick = onSettingClick, shapes = IconButtonDefaults.shapes()) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_setting),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp),
                        tint = MaterialTheme.colorScheme.surface
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
    )
}