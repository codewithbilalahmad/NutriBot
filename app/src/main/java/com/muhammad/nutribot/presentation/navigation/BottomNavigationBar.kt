package com.muhammad.nutribot.presentation.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.muhammad.nutribot.R
import com.muhammad.nutribot.utils.rippleClickable

@Composable
fun BottomNavigationBar(modifier: Modifier = Modifier, navHostController: NavHostController) {
    val currentDestination = navHostController.currentBackStackEntryAsState().value?.destination
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 8.dp)
                .background(MaterialTheme.colorScheme.background),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .navigationBarsPadding()
                    .padding(bottom = 8.dp)
                    .background(MaterialTheme.colorScheme.background),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BottomNavigationItem(isSelected = currentDestination?.hierarchy?.any {
                    it.route == BottomNavItem.Diary.route::class.qualifiedName
                } == true, navItem = BottomNavItem.Diary, navHostController = navHostController)
                BottomNavigationItem(isSelected = currentDestination?.hierarchy?.any {
                    it.route == BottomNavItem.Progress.route::class.qualifiedName
                } == true, navItem = BottomNavItem.Progress, navHostController = navHostController)
            }
        }
        LargeFloatingActionButton(
            onClick = {},
            shape = CircleShape,
            modifier = Modifier.size(85.dp).graphicsLayer{
                translationY = -50.dp.toPx()
            },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_add),
                contentDescription = null, modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun BottomNavigationItem(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    isSelected: Boolean,
    navItem: BottomNavItem,
) {
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
        label = "contentColor"
    )
    Column(
        modifier = modifier.rippleClickable {
            navHostController.navigate(navItem.route){
                popUpTo(navHostController.graph.findStartDestination().id) {
                    saveState = true
                }

                launchSingleTop = true
                restoreState = true
            }
        },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(navItem.icon),
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(30.dp)
        )
        Text(
            text = stringResource(navItem.title),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isSelected) FontWeight.Normal else FontWeight.Light,
                color = contentColor
            )
        )
    }
}