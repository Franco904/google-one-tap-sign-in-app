package com.example.one_tap_sign_in.core.data.dataSources.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import com.example.one_tap_sign_in.core.data.dataSources.connectivity.interfaces.ConnectivityObserver
import com.example.one_tap_sign_in.core.data.exceptions.ConnectivityException
import com.example.one_tap_sign_in.core.data.exceptions.toConnectivityException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

class ConnectivityObserverImpl(
    appContext: Context,
) : ConnectivityObserver {
    private val connectivityManager by lazy {
        appContext.getSystemService<ConnectivityManager>()
            ?: throw ConnectivityException.ConnectivityManagerNotFound()
    }

    override fun isConnectedToInternet() = callbackFlow {
        val callback = object : NetworkCallback() {
            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)

                val isConnected = networkCapabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_VALIDATED,
                )
                trySend(isConnected)
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                trySend(true)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                trySend(false)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                trySend(false)
            }
        }

        try {
            connectivityManager.registerDefaultNetworkCallback(callback)
        } catch (e: Exception) {
            throw e.toConnectivityException()
        }

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
        .flowOn(Dispatchers.IO)
}
