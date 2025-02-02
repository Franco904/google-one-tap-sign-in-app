package com.example.one_tap_sign_in.core.domain.utils

sealed interface DataSourceError : Error {
    enum class PreferencesError : DataSourceError {
        SerializationError,
        SecurityError,
        UnknownError,
    }

    enum class HttpError : DataSourceError {
        BadRequest,
        Unauthorized,
        Forbidden,
        NotFound,
        ServerError,
        UnknownHttpRemoteError,
        SerializationError,
        Timeout,
        NetworkError,
        UnknownError,
    }
}
