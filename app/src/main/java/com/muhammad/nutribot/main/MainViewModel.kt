package com.muhammad.nutribot.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammad.nutribot.domain.repository.connection.ConnectivityObserver
import com.muhammad.nutribot.domain.repository.settings.SettingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class MainViewModel(
    private val settingRepository: SettingRepository,
    private val connectivityObserver: ConnectivityObserver,
) : ViewModel() {
    private val _state = MutableStateFlow(MainAppState())
    val state = _state.asStateFlow()

    init {
        observeIsUserLoggedIn()
        observeIsInternetConnected()
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