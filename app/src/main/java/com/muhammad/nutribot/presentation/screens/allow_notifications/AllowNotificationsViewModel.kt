package com.muhammad.nutribot.presentation.screens.allow_notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammad.nutribot.domain.repository.settings.SettingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AllowNotificationsViewModel(
    private val settingRepository: SettingRepository
) : ViewModel(){
    private val _state= MutableStateFlow(AllowNotificationsState())
    val state = _state.asStateFlow()
    fun onAction(action: AllowNotificationsAction){
        when(action){
            AllowNotificationsAction.OnToggleNotificationPermissionDeniedDialog -> onToggleNotificationPermissionDeniedDialog()
            AllowNotificationsAction.OnStartNutritionPlan -> onStartNutritionPlan()
        }
    }

    private fun onStartNutritionPlan() {
        viewModelScope.launch(Dispatchers.IO){
            settingRepository.saveIsUserLoggedIn(true)
        }
    }

    private fun onToggleNotificationPermissionDeniedDialog() {
        _state.update { it.copy(showNotificationPermissionDeniedDialog = !it.showNotificationPermissionDeniedDialog) }
    }
}