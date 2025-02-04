package com.example.one_tap_sign_in.core.data.dataSources.connectivity.interfaces

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun isConnectedToInternet(): Flow<Boolean>
}
