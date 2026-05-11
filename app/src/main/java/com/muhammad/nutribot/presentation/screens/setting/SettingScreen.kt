package com.muhammad.nutribot.presentation.screens.setting

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.muhammad.nutribot.R
import com.muhammad.nutribot.domain.model.ActivityLevel
import com.muhammad.nutribot.domain.model.Gender
import com.muhammad.nutribot.presentation.components.button.PrimaryButton
import com.muhammad.nutribot.presentation.components.wheel_picker.WheelPicker
import com.muhammad.nutribot.presentation.components.wheel_picker.WheelPickerHorizontal
import com.muhammad.nutribot.presentation.screens.nurition_setup.components.ActivityLevelCard
import com.muhammad.nutribot.presentation.screens.setting.components.GenderCard
import com.muhammad.nutribot.presentation.screens.setting.components.SettingHeader
import com.muhammad.nutribot.presentation.screens.setting.components.SettingItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingScreen(
    navHostController: NavHostController,
    viewModel: SettingViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    BackHandler {
        when {
            state.showAgeSection -> {
                viewModel.onAction(SettingAction.OnToggleAgeSection)
            }

            state.showGenderSection -> {
                viewModel.onAction(SettingAction.OnToggleGenderSection)
            }

            state.showHeightAndHeightSection -> {
                viewModel.onAction(SettingAction.OnToggleHeightAndWeightSection)
            }

            state.showActivityLevelSection -> {
                viewModel.onAction(SettingAction.OnToggleActivityLevelSection)
            }

            else -> navHostController.navigateUp()
        }
    }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(text = stringResource(R.string.settings))
            },
            navigationIcon = {
                IconButton(onClick = {
                    when {
                        state.showAgeSection -> {
                            viewModel.onAction(SettingAction.OnToggleAgeSection)
                        }

                        state.showGenderSection -> {
                            viewModel.onAction(SettingAction.OnToggleGenderSection)
                        }

                        state.showHeightAndHeightSection -> {
                            viewModel.onAction(SettingAction.OnToggleHeightAndWeightSection)
                        }

                        state.showActivityLevelSection -> {
                            viewModel.onAction(SettingAction.OnToggleActivityLevelSection)
                        }

                        else -> navHostController.navigateUp()
                    }
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
                modifier = Modifier.fillMaxSize()
            ) {
                item("topbar_divider") {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
                item("profile_header") {
                    SettingHeader(label = R.string.profile)
                }
                item("reminders") {
                    SettingItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        label = R.string.enable_reminder,
                        trailingContent = {
                            Switch(checked = state.reminderEnabled, onCheckedChange = { enable ->
                                viewModel.onAction(SettingAction.OnToggleReminderEnabled(enable))
                            })
                        },
                        onClick = {
                            viewModel.onAction(SettingAction.OnToggleReminderEnabled(!state.reminderEnabled))
                        })
                }
                item("username") {
                    SettingItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        onClick = {
                            viewModel.onAction(SettingAction.OnToggleGenderSection)
                        },
                        label = R.string.username, value = state.username
                    )
                }
                item("gender") {
                    SettingItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        onClick = {
                            viewModel.onAction(SettingAction.OnToggleGenderSection)
                        },
                        label = R.string.gender, value = stringResource(state.gender.value)
                    )
                }
                item("age") {
                    SettingItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        onClick = {
                            viewModel.onAction(SettingAction.OnToggleAgeSection)
                        },
                        label = R.string.age, value = state.age.toString()
                    )
                }
                item("height") {
                    SettingItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        onClick = {
                            viewModel.onAction(SettingAction.OnToggleHeightAndWeightSection)
                        },
                        label = R.string.height,
                        value = "${state.height} ${stringResource(R.string.cm)}"
                    )
                }
                item("weight") {
                    SettingItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        onClick = {
                            viewModel.onAction(SettingAction.OnToggleHeightAndWeightSection)
                        },
                        label = R.string.weight,
                        value = "${state.weight} ${stringResource(R.string.kg)}"
                    )
                }
                item("activity_level") {
                    SettingItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        onClick = {
                            viewModel.onAction(SettingAction.OnToggleActivityLevelSection)
                        },
                        label = R.string.activity_level,
                        value = stringResource(state.activityLevel.label)
                    )
                }
                item("profile_divider") {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
                item("about_header") {
                    SettingHeader(label = R.string.about, modifier = Modifier.animateItem())
                }
                item(key = "privacy_policy") {
                    AboutItem(
                        title = R.string.privacy_policy,
                        icon = R.drawable.ic_privacy,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(), onClick = {}
                    )
                }
                item(key = "contact_us") {
                    AboutItem(
                        title = R.string.contact_us,
                        icon = R.drawable.ic_contact_us,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(), onClick = {}
                    )
                }
                item(key = "rate_and_review") {
                    AboutItem(
                        title = R.string.rate_and_review,
                        icon = R.drawable.ic_rate,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(), onClick = {}
                    )
                }
                item(key = "share") {
                    AboutItem(
                        title = R.string.share_app,
                        icon = R.drawable.ic_share,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(), onClick = {}
                    )
                }
                item(key = "credits") {
                    AboutItem(
                        title = R.string.credits,
                        icon = R.drawable.ic_info,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(), onClick = {}
                    )
                }
            }
            AnimatedVisibility(
                visible = state.showGenderSection,
                enter = fadeIn(),
                exit = fadeOut(), modifier = Modifier.fillMaxSize()
            ) {
                var selectedGender by remember { mutableStateOf(state.gender) }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .verticalScroll(rememberScrollState())
                        .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.gender_title),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.gender_desp),
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.surface)
                    )
                    Spacer(Modifier.height(24.dp))
                    Gender.entries.forEach { gender ->
                        val isSelected = gender == selectedGender
                        GenderCard(
                            modifier = Modifier.fillMaxWidth(),
                            gender = gender,
                            isSelected = isSelected, onSelectGender = { gender ->
                                selectedGender = gender
                            }
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                    Spacer(Modifier.weight(1f))
                    PrimaryButton(
                        text = stringResource(R.string.save),
                        onClick = {
                            state.userProfile?.let { profile ->
                                viewModel.onAction(SettingAction.OnToggleGenderSection)
                                viewModel.onAction(SettingAction.OnChangeProfile(profile.copy(gender = selectedGender)))
                            }
                        },
                        enabled = selectedGender != state.gender,
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    )
                }
            }
            AnimatedVisibility(
                visible = state.showAgeSection,
                enter = fadeIn(),
                exit = fadeOut(), modifier = Modifier.fillMaxSize()
            ) {
                var selectedAge by remember { mutableIntStateOf(state.age) }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .verticalScroll(rememberScrollState())
                        .padding(top = 32.dp, bottom = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.what_your_age),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.what_your_age_desp),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.surface,
                            textAlign = TextAlign.Center
                        )
                    )
                    Spacer(Modifier.height(120.dp))
                    WheelPickerHorizontal(
                        range = 4..100,
                        modifier = Modifier.fillMaxWidth(),
                        initialValue = state.userProfile?.age ?: 0,
                        onItemSelected = { age ->
                            selectedAge = age
                        })
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.years),
                        style = MaterialTheme.typography.titleSmall.copy(textAlign = TextAlign.Center)
                    )
                    Spacer(Modifier.weight(1f))
                    PrimaryButton(
                        text = stringResource(R.string.save),
                        onClick = {
                            state.userProfile?.let { profile ->
                                viewModel.onAction(SettingAction.OnToggleAgeSection)
                                viewModel.onAction(SettingAction.OnChangeProfile(profile.copy(age = selectedAge)))
                            }
                        },
                        enabled = selectedAge != state.age,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    )
                }
            }
            AnimatedVisibility(
                visible = state.showHeightAndHeightSection,
                enter = fadeIn(),
                exit = fadeOut(), modifier = Modifier.fillMaxSize()
            ) {
                var selectedHeight by remember {
                    mutableIntStateOf(state.height)
                }
                var selectedWeight by remember {
                    mutableIntStateOf(state.weight)
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .verticalScroll(rememberScrollState())
                        .padding(top = 32.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.what_is_your_wh),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.what_is_your_wh_desp),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.surface,
                            textAlign = TextAlign.Center
                        )
                    )
                    Spacer(Modifier.height(120.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            32.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        Column(
                            Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.height),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            WheelPicker(
                                range = 60..243,
                                initialValue = state.userProfile?.heightCm ?: 0,
                                label = R.string.cm, onItemSelected = { height ->
                                    selectedHeight = height
                                }
                            )
                        }
                        Column(
                            Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.weight),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            WheelPicker(
                                range = 20..360,
                                initialValue = state.userProfile?.weightKg ?: 0,
                                label = R.string.kg, onItemSelected = { weight ->
                                    selectedWeight = weight
                                }
                            )
                        }
                    }
                    Spacer(Modifier.weight(1f))
                    PrimaryButton(
                        text = stringResource(R.string.save),
                        onClick = {
                            state.userProfile?.let { profile ->
                                viewModel.onAction(SettingAction.OnToggleHeightAndWeightSection)
                                viewModel.onAction(SettingAction.OnChangeProfile(profile.copy(heightCm = selectedHeight, weightKg = selectedWeight)))
                            }
                        },
                        enabled = selectedHeight != state.height || selectedWeight != state.weight,
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    )
                }
            }
            AnimatedVisibility(
                visible = state.showActivityLevelSection,
                enter = fadeIn(),
                exit = fadeOut(), modifier = Modifier.fillMaxSize()
            ) {
                var selectedActivityLevel by remember { mutableStateOf(state.activityLevel) }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .verticalScroll(rememberScrollState())
                        .padding(top = 32.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.what_is_your_lifecycle),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.what_is_your_lifecycle_desp),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.surface,
                            textAlign = TextAlign.Center
                        )
                    )
                    Spacer(Modifier.height(24.dp))
                    ActivityLevel.entries.forEach { level ->
                        ActivityLevelCard(activityLevel = level, onSelectActivityLevel = {level ->
                            selectedActivityLevel = level
                        }, isSelected = selectedActivityLevel == level)
                        Spacer(Modifier.height(8.dp))
                    }
                    Spacer(Modifier.weight(1f))
                    PrimaryButton(
                        text = stringResource(R.string.save),
                        onClick = {
                            state.userProfile?.let { profile ->
                                viewModel.onAction(SettingAction.OnToggleActivityLevelSection)
                                viewModel.onAction(SettingAction.OnChangeProfile(profile.copy(activityLevel = selectedActivityLevel)))
                            }
                        },
                        enabled = selectedActivityLevel != state.activityLevel,
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    )
                }
            }
        }
    }
}