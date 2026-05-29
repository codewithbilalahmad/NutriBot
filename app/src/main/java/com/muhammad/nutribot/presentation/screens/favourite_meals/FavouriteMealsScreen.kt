package com.muhammad.nutribot.presentation.screens.favourite_meals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.muhammad.nutribot.R
import com.muhammad.nutribot.presentation.screens.favourite_meals.components.FavouriteMealCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavouriteMealsScreen(
    navHostController: NavHostController,
    viewModel: FavouriteMealsViewModel = koinViewModel(),
) {
    val layoutDirection = LocalLayoutDirection.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(text = stringResource(R.string.favourites))
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
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
        )
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 32.dp
                ), verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.favouriteMeals, key = { it.id }, contentType = {
                    "favourite_meal_${it.id}"
                }) { favouriteMeal ->
                    FavouriteMealCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        meal = favouriteMeal,
                        onClick = {

                        })
                }
            }
            Box(modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter).height(50.dp)
                .background(brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background.copy(0.7f),
                        MaterialTheme.colorScheme.background.copy(0.6f),
                        MaterialTheme.colorScheme.background.copy(0.5f),
                        MaterialTheme.colorScheme.background.copy(0.4f),
                        MaterialTheme.colorScheme.background.copy(0.3f),
                        Color.Transparent
                    )
                )))
            Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).height(50.dp)
                .background(brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        MaterialTheme.colorScheme.background.copy(0.3f),
                        MaterialTheme.colorScheme.background.copy(0.4f),
                        MaterialTheme.colorScheme.background.copy(0.5f),
                        MaterialTheme.colorScheme.background.copy(0.6f),
                        MaterialTheme.colorScheme.background.copy(0.7f),
                    )
                )))

        }
    }
}