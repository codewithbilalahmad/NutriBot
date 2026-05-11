package com.muhammad.nutribot.domain.repository.connection

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    val isConnected : Flow<Boolean>
}