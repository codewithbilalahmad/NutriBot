package com.muhammad.nutribot.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammad.nutribot.NutriBotApplication
import com.muhammad.nutribot.domain.repository.connection.ConnectivityObserver
import com.muhammad.nutribot.domain.repository.settings.SettingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val settingRepository: SettingRepository,
    private val connectivityObserver: ConnectivityObserver,
) : ViewModel() {
    private val context = NutriBotApplication.INSTANCE
    private val _state = MutableStateFlow(MainAppState())
    val state = _state.asStateFlow()

    init {
        observeIsUserLoggedIn()
        observeIsInternetConnected()
        checkNotificationPermission()
    }

    fun checkNotificationPermission() {
        val granted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else true
        viewModelScope.launch {
            if(!granted){
                settingRepository.saveEnableReminder(false)
            }
        }
    }

    private fun observeIsInternetConnected() {
        connectivityObserver.isConnected.onEach { isConnected ->
            _state.update { it.copy(isInternetConnected = isConnected) }
        }.launchIn(viewModelScope)
    }

    private fun observeIsUserLoggedIn() {
        settingRepository.observeIsUserLoggedIn().onEach { isUserLoggedIn ->
            _state.update {
                it.copy(isUserLoggedIn = isUserLoggedIn, isCheckingLogin = false)
            }
        }.launchIn(viewModelScope)
    }
}