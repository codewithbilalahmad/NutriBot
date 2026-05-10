package com.muhammad.nutribot.presentation.screens.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammad.nutribot.domain.repository.settings.SettingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingViewModel(
    private val settingRepository: SettingRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(SettingState())
    val state = combine(
        _state,
        settingRepository.observeUserProfile(),
        settingRepository.observeEnableReminder()
    ) { state, userProfile, reminderEnabled ->
        state.copy(userProfile = userProfile, reminderEnabled = reminderEnabled)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, SettingState())

    fun onAction(action: SettingAction) {
        when (action) {
            SettingAction.OnToggleActivityLevelSection -> onToggleActivityLevelSection()
            SettingAction.OnToggleAgeSection -> onToggleAgeSection()
            SettingAction.OnToggleGenderSection -> onToggleGenderSection()
            SettingAction.OnToggleHeightAndWeightSection -> onToggleHeightAndWeightSection()
            is SettingAction.OnToggleReminderEnabled -> onToggleReminderEnabled(action.enable)
        }
    }

    private fun onToggleReminderEnabled(enable: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingRepository.saveEnableReminder(enable)
        }
    }

    private fun onToggleHeightAndWeightSection() {
        _state.update { it.copy(showHeightAndHeightSection = !it.showHeightAndHeightSection) }
    }

    private fun onToggleGenderSection() {
        _state.update { it.copy(showGenderSection = !it.showGenderSection) }
    }

    private fun onToggleAgeSection() {
        _state.update { it.copy(showAgeSection = !it.showAgeSection) }
    }

    private fun onToggleActivityLevelSection() {
        _state.update { it.copy(showActivityLevelSection = !it.showActivityLevelSection) }
    }
}