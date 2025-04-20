package com.example.one_tap_sign_in.core.domain.utils

sealed interface DataSourceError : Error {
    enum class PreferencesError : DataSourceError {
        SerializationError,
        SecurityError,
        UnknownError,
    }

    enum class RemoteBackendError : DataSourceError {
        BadRequest,
        Unauthorized,
        NotFound,
        ServerError,
        UnknownHttpRemoteError,
        SerializationError,
        Timeout,
        NetworkError,
        UnknownError
    }

    fun isRedirectError(): Boolean {
        return this in listOf(
            RemoteBackendError.Unauthorized,
            RemoteBackendError.NotFound,
        )
    }
}
