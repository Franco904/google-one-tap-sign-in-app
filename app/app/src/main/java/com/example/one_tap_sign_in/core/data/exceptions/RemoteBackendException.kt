package com.example.one_tap_sign_in.core.data.exceptions

import com.example.one_tap_sign_in.core.domain.utils.DataSourceError
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import java.net.ConnectException
import java.net.UnknownHostException

sealed class RemoteBackendException(message: String) : Exception(message) {
    // HTTP errors
    class BadRequest : RemoteBackendException("Invalid request body or headers.")
    class Unauthorized : RemoteBackendException("Invalid credentials or session expired.")
    class NotFound : RemoteBackendException("Requested resource not found.")
    class ServerError(statusCode: String) :
        RemoteBackendException("Server error: $statusCode code. Please try again later.")

    class UnknownRemoteBackendRemoteError(statusCode: String) :
        RemoteBackendException("Unexpected response: $statusCode code.")

    // Other errors
    class SerializationError(message: String?) :
        RemoteBackendException("Request or response data parsing failed, unexpected response format. $message.")

    class Timeout(message: String?) : RemoteBackendException("Request timed out: $message")
    class NetworkError(message: String?) :
        RemoteBackendException("Network/Internet connection failed. Message: $message")

    class UnknownError(message: String?) : RemoteBackendException("Unknown error: $message.")

    fun toRemoteBackendError() = when (this) {
        is BadRequest -> DataSourceError.RemoteBackendError.BadRequest
        is Unauthorized -> DataSourceError.RemoteBackendError.Unauthorized
        is NotFound -> DataSourceError.RemoteBackendError.NotFound
        is ServerError -> DataSourceError.RemoteBackendError.ServerError
        is UnknownRemoteBackendRemoteError -> DataSourceError.RemoteBackendError.UnknownHttpRemoteError
        is SerializationError -> DataSourceError.RemoteBackendError.SerializationError
        is Timeout -> DataSourceError.RemoteBackendError.Timeout
        is NetworkError -> DataSourceError.RemoteBackendError.NetworkError
        is UnknownError -> DataSourceError.RemoteBackendError.UnknownError
    }
}

fun HttpStatusCode.toRemoteBackendException(): RemoteBackendException {
    return when (this) {
        HttpStatusCode.BadRequest -> RemoteBackendException.BadRequest()
        HttpStatusCode.Unauthorized -> RemoteBackendException.Unauthorized()
        HttpStatusCode.NotFound -> RemoteBackendException.NotFound()
        else -> {
            if (value in 500..599) {
                RemoteBackendException.ServerError(statusCode = "$value")
            } else {
                RemoteBackendException.UnknownRemoteBackendRemoteError(statusCode = "$value")
            }
        }
    }
}

fun Exception.toRemoteBackendException(): RemoteBackendException {
    return when (this) {
        is SerializationException -> RemoteBackendException.SerializationError(message = "$cause: $message")
        is HttpRequestTimeoutException -> RemoteBackendException.Timeout(message = "$cause: $message")
        is ConnectException -> RemoteBackendException.NetworkError(message = "$cause: $message")
        is UnknownHostException -> RemoteBackendException.NetworkError(message = "$cause: $message")
        is IOException -> RemoteBackendException.NetworkError(message = "$cause: $message")
        else -> RemoteBackendException.UnknownError(message = "$cause: $message")
    }
}
