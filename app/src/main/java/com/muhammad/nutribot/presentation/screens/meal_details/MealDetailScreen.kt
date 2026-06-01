package com.muhammad.nutribot.presentation.screens.meal_details

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.muhammad.nutribot.R
import com.muhammad.nutribot.domain.model.EditMealOption
import com.muhammad.nutribot.presentation.components.button.PrimaryButton
import com.muhammad.nutribot.presentation.components.button.SecondaryButton
import com.muhammad.nutribot.presentation.components.image.AppImage
import com.muhammad.nutribot.presentation.components.image.ImagePlaceholder
import com.muhammad.nutribot.presentation.components.textfield.AppTextField
import com.muhammad.nutribot.presentation.navigation.Destination
import com.muhammad.nutribot.presentation.screens.meal_details.components.HeartLikeAnimation
import com.muhammad.nutribot.presentation.screens.meal_details.components.MealDateCard
import com.muhammad.nutribot.presentation.screens.meal_details.components.MealIngredientCard
import com.muhammad.nutribot.presentation.screens.meal_details.components.MealNutritionSection
import com.muhammad.nutribot.presentation.screens.meal_details.components.NumberOfServingsSection
import com.muhammad.nutribot.presentation.screens.meal_details.components.TotalCaloriesCard
import com.muhammad.nutribot.utils.ObserveAsEvents
import com.muhammad.nutribot.utils.isNetworkUrl
import com.muhammad.nutribot.utils.loadingEffect
import com.muhammad.nutribot.utils.rippleClickable
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDateTime
import org.koin.androidx.compose.koinViewModel
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
fun MealDetailScreen(
    navHostController: NavHostController,
    viewModel: MealDetailViewModel = koinViewModel(),
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val density = LocalDensity.current
    val gradientHeight = 100.dp
    val gradientHeightPx = with(density) {
        gradientHeight.toPx()
    }
    val layoutDirection = LocalLayoutDirection.current
    val listState = rememberLazyListState()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val food = state.food
    val heartScale = remember { Animatable(1f) }
    var showLikeAnimation by remember { mutableStateOf(false) }
    LaunchedEffect(food.isFavorite) {
        if (food.isFavorite) {
            showLikeAnimation = true
            heartScale.snapTo(0f)
            launch {
                heartScale.animateTo(
                    targetValue = 1.4f,
                    animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
                )
                heartScale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
                )
            }
        }
    }
    val datePickerState = rememberDatePickerState(
        initialSelectedDate = state.selectedDate.toJavaLocalDate(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val today = Clock.System.now()
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .date
                val date = Instant.fromEpochMilliseconds(utcTimeMillis)
                    .toLocalDateTime(TimeZone.currentSystemDefault()).date
                return date <= today
            }
        })
    val galleryImagePicker =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.onAction(MealDetailAction.OnSelectMealImage(uri.toString()))
            }
        }
    val scrollOffset by remember {
        derivedStateOf {
            listState.firstVisibleItemScrollOffset
        }
    }
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            MealDetailEvent.OnMealLoggedSuccess -> {
                if (state.isAlreadyLogged) {
                    navHostController.navigate(Destination.DiaryScreen) {
                        popUpTo<Destination.MealDetailScreen> {
                            inclusive = true
                        }
                    }
                } else {
                    navHostController.navigate(Destination.StreakProgressScreen) {
                        popUpTo<Destination.MealDetailScreen> {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(), bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                PrimaryButton(
                    text = if (state.isAlreadyLogged) stringResource(R.string.done) else "${
                        stringResource(
                            R.string.log_to
                        )
                    } ${state.selectedDate.day} ${
                        state.selectedDate.month.name.lowercase()
                            .replaceFirstChar { it.uppercase() }
                    }",
                    onClick = {
                        viewModel.onAction(MealDetailAction.OnLogToMeal)
                    },
                    contentPadding = PaddingValues(vertical = 16.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    PaddingValues(
                        start = paddingValues.calculateStartPadding(layoutDirection),
                        end = paddingValues.calculateEndPadding(layoutDirection),
                        bottom = paddingValues.calculateBottomPadding()
                    )
                ).clickable(interactionSource = remember { MutableInteractionSource() },indication = null){
                    galleryImagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                if (food.mealImageUrl.isNotEmpty()) {
                    with(sharedTransitionScope) {
                        if (food.mealImageUrl.isNetworkUrl()) {
                            AsyncImage(
                                model = food.mealImageUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .graphicsLayer {
                                        translationY = with(density) {
                                            -scrollOffset.toFloat() * 0.5f
                                        }
                                    }
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.surfaceContainer)
                                    .loadingEffect()
                                    .sharedElement(
                                        sharedContentState = rememberSharedContentState(
                                            key = "meal_image"
                                        ),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                        boundsTransform = { _, _ ->
                                            tween(durationMillis = 300, easing = LinearEasing)
                                        }
                                    ))
                        } else {
                            AppImage(
                                image = food.mealImageUrl,
                                modifier = Modifier
                                    .graphicsLayer {
                                        translationY = with(density) {
                                            -scrollOffset.toFloat() * 0.5f
                                        }
                                    }
                                    .fillMaxSize()
                                    .sharedElement(
                                        sharedContentState = rememberSharedContentState(
                                            key = "meal_image"
                                        ),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                        boundsTransform = { _, _ ->
                                            tween(durationMillis = 300, easing = LinearEasing)
                                        }
                                    )
                            )
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                translationY = with(density) {
                                    -scrollOffset.toFloat() * 0.5f
                                }
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        ImagePlaceholder()
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.tap_to_upload_image),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = MaterialTheme.colorScheme.surface,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
                HeartLikeAnimation(
                    showAnimation = showLikeAnimation,
                    onAnimationFinished = {
                        showLikeAnimation = false
                    }
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 278.dp, bottom = 100.dp),
                state = listState
            ) {
                item("fav_icon") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem()
                            .padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        IconButton(
                            onClick = {
                                viewModel.onAction(MealDetailAction.OnToggleMealFavourite())
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.Black.copy(alpha = 0.3f)
                            )
                        ) {
                            val icon =
                                if (food.isFavorite)
                                    R.drawable.ic_favourite_filled
                                else
                                    R.drawable.ic_favourite_outlined

                            val tint =
                                if (food.isFavorite)
                                    MaterialTheme.colorScheme.error
                                else
                                    MaterialTheme.colorScheme.surfaceVariant

                            Icon(
                                imageVector = ImageVector.vectorResource(icon),
                                contentDescription = null,
                                modifier = Modifier.graphicsLayer {
                                    scaleX = heartScale.value
                                    scaleY = heartScale.value
                                },
                                tint = tint
                            )
                        }
                    }
                }
                item("food_details") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp
                                )
                            )
                            .background(MaterialTheme.colorScheme.background)
                            .animateItem()
                            .padding(top = 8.dp)
                    ) {
                        Text(
                            text = food.name,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .padding(horizontal = 4.dp)
                                .clickable {}
                                .padding(
                                    horizontal = 12.dp,
                                    vertical = 6.dp
                                )
                                .rippleClickable(onClick = {
                                    viewModel.onAction(
                                        MealDetailAction.OnEditMealOptionClick(
                                            EditMealOption.NAME
                                        )
                                    )
                                })
                        )

                        Spacer(Modifier.height(12.dp))

                        NumberOfServingsSection(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            food = food,
                            onAddServings = {
                                viewModel.onAction(
                                    MealDetailAction.OnChangeMealNumberOfServings(
                                        (food.numberOfServings + 1).coerceAtMost(
                                            10
                                        )
                                    )
                                )
                            },
                            onMinusServings = {
                                viewModel.onAction(
                                    MealDetailAction.OnChangeMealNumberOfServings(
                                        (food.numberOfServings - 1).coerceAtLeast(
                                            1
                                        )
                                    )
                                )
                            }
                        )

                        Spacer(Modifier.height(16.dp))

                        TotalCaloriesCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            food = food,
                            onClick = {
                                viewModel.onAction(
                                    MealDetailAction.OnEditMealOptionClick(
                                        EditMealOption.CALORIES
                                    )
                                )
                            }
                        )

                        Spacer(Modifier.height(16.dp))
                        MealNutritionSection(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            food = food,
                            onFatClick = {
                                viewModel.onAction(
                                    MealDetailAction.OnEditMealOptionClick(
                                        EditMealOption.FATS
                                    )
                                )
                            },
                            onCarbsClick = {
                                viewModel.onAction(
                                    MealDetailAction.OnEditMealOptionClick(
                                        EditMealOption.CARBS
                                    )
                                )
                            },
                            onProteinClick = {
                                viewModel.onAction(
                                    MealDetailAction.OnEditMealOptionClick(
                                        EditMealOption.PROTEIN
                                    )
                                )
                            }
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.ingredients_found),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(Modifier.height(16.dp))
                        if (food.ingredients.isNotEmpty()) {
                            food.ingredients.forEach { ingredient ->
                                MealIngredientCard(
                                    ingredient = ingredient,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    onClick = {
                                        viewModel.onAction(
                                            MealDetailAction.OnToggleIngredientSelection(
                                                id = ingredient.id
                                            )
                                        )
                                    })
                                Spacer(Modifier.height(8.dp))
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ingredients),
                                    contentDescription = null,
                                    modifier = Modifier.size(150.dp)
                                )
                                Text(
                                    text = stringResource(R.string.no_ingredients_found),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .graphicsLayer {
                        translationY = (-gradientHeightPx + (scrollOffset * 0.5f)).coerceIn(
                            -gradientHeightPx,
                            0f
                        )
                    }
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .height(gradientHeight)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.background.copy(0.8f),
                                MaterialTheme.colorScheme.background.copy(0.7f),
                                MaterialTheme.colorScheme.background.copy(0.6f),
                                MaterialTheme.colorScheme.background.copy(0.5f),
                                Color.Transparent
                            )
                        )
                    )
            )

            IconButton(
                onClick = {
                    navHostController.navigateUp()
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Black.copy(alpha = 0.3f),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {

                Icon(
                    imageVector = ImageVector.vectorResource(
                        R.drawable.ic_arrow_left
                    ),
                    contentDescription = null
                )
            }
            if (!state.isAlreadyLogged) {
                MealDateCard(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp),
                    selectedDate = state.selectedDate, onClick = {
                        viewModel.onAction(MealDetailAction.OnToggleDatePickerDialog)
                    }
                )
            }
        }
    }
    if (state.showEditMealDialog) {
        AlertDialog(onDismissRequest = {
            viewModel.onAction(MealDetailAction.OnDismissEditMealDialog)
        }, title = {
            val title = when (state.selectedEditMealOption) {
                EditMealOption.NONE -> ""
                EditMealOption.NAME -> {
                    stringResource(R.string.edit_food_name_title)
                }

                EditMealOption.CALORIES -> {
                    stringResource(R.string.edit_calories_title)
                }

                EditMealOption.FATS -> {
                    stringResource(R.string.edit_fat_title)
                }

                EditMealOption.CARBS -> {
                    stringResource(R.string.edit_carbs_title)
                }

                EditMealOption.PROTEIN -> {
                    stringResource(R.string.edit_protein_title)
                }
            }
            val desp = when (state.selectedEditMealOption) {
                EditMealOption.NONE -> ""
                EditMealOption.NAME -> {
                    stringResource(R.string.edit_food_name_desp)
                }

                EditMealOption.CALORIES -> {
                    stringResource(R.string.edit_calories_desp)
                }

                EditMealOption.FATS -> {
                    stringResource(R.string.edit_fat_desp)
                }

                EditMealOption.CARBS -> {
                    stringResource(R.string.edit_carbs_desp)
                }

                EditMealOption.PROTEIN -> {
                    stringResource(R.string.edit_protein_desp)
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = desp,
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.surface)
                )
            }
        }, containerColor = MaterialTheme.colorScheme.background, text = {
            AppTextField(
                state = state.editMealState,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = R.drawable.ic_cancel_filled,
                onTrailingClick = {
                    viewModel.onAction(MealDetailAction.OnClearEditMealState)
                },
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Light)
            )
        }, dismissButton = {
            SecondaryButton(text = stringResource(R.string.cancel), onClick = {
                viewModel.onAction(MealDetailAction.OnDismissEditMealDialog)
            })
        }, confirmButton = {
            PrimaryButton(text = stringResource(R.string.save), onClick = {
                viewModel.onAction(MealDetailAction.OnSaveEditMealOption)
            })
        })
    }
    if (state.showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = { viewModel.onAction(MealDetailAction.OnToggleDatePickerDialog) },
            dismissButton = {
                SecondaryButton(text = stringResource(R.string.cancel), onClick = {
                    viewModel.onAction(MealDetailAction.OnToggleDatePickerDialog)
                })
            }, confirmButton = {
                PrimaryButton(text = stringResource(R.string.done), onClick = {
                    viewModel.onAction(MealDetailAction.OnToggleDatePickerDialog)
                    val selectedDateMillis = datePickerState.selectedDateMillis
                    if (selectedDateMillis != null) {
                        val selectedDate =
                            Instant.fromEpochMilliseconds(selectedDateMillis).toLocalDateTime(
                                TimeZone.currentSystemDefault()
                            ).date
                        viewModel.onAction(MealDetailAction.OnChangeSelectedDate(date = selectedDate))
                    }
                })
            }, content = {
                DatePicker(state = datePickerState)
            })
    }
}