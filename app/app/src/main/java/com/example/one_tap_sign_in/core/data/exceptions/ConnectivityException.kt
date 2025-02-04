package com.example.one_tap_sign_in.core.data.exceptions

sealed class ConnectivityException(message: String) : Exception(message) {
    class ConnectivityManagerNotFound
        : ConnectivityException("Failed to found connectivity manager on app's context.")

    class TooManyNetworkCallbacks(message: String) :
        ConnectivityException("There is too many network callbacks registered: $message")

    class UnknownError(message: String?) : ConnectivityException("Unknown error: $message.")
}

fun Exception.toConnectivityException(): ConnectivityException {
    return when (this) {
        is RuntimeException -> ConnectivityException.TooManyNetworkCallbacks(message = "$cause: $message")
        else -> ConnectivityException.UnknownError(message = "$cause: $message")
    }
}
