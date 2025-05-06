package com.example.applearndriver.utils.network

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observe(): Flow<Status>

    enum class Status {
        AVAILABLE,
        LOST_CONNECTION
    }
}
