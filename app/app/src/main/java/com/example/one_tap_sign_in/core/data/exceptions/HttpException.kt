package com.example.one_tap_sign_in.core.data.exceptions

import com.example.one_tap_sign_in.core.domain.utils.DataSourceError
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import java.net.ConnectException
import java.net.UnknownHostException

sealed class HttpException(message: String) : Exception(message) {
    // HTTP errors
    class BadRequest : HttpException("Invalid request body or headers.")
    class Unauthorized : HttpException("Invalid credentials or session expired.")
    class Forbidden : HttpException("Access denied.")
    class NotFound : HttpException("Requested resource not found.")
    class ServerError(statusCode: String) :
        HttpException("Server error: $statusCode code. Please try again later.")

    class UnknownHttpRemoteError(statusCode: String) :
        HttpException("Unexpected response: $statusCode code.")

    // Other errors
    class SerializationError(message: String?) :
        HttpException("Request or response data parsing failed, unexpected response format. $message.")

    class Timeout(message: String?) : HttpException("Request timed out: $message")
    class NetworkError(message: String?) :
        HttpException("Network/Internet connection failed. Message: $message")

    class UnknownError(message: String?) : HttpException("Unknown error: $message.")

    fun toBackendError() = when (this) {
        is BadRequest -> DataSourceError.HttpError.BadRequest
        is Unauthorized -> DataSourceError.HttpError.Unauthorized
        is Forbidden -> DataSourceError.HttpError.Forbidden
        is NotFound -> DataSourceError.HttpError.NotFound
        is ServerError -> DataSourceError.HttpError.ServerError
        is UnknownHttpRemoteError -> DataSourceError.HttpError.UnknownHttpRemoteError
        is SerializationError -> DataSourceError.HttpError.SerializationError
        is Timeout -> DataSourceError.HttpError.Timeout
        is NetworkError -> DataSourceError.HttpError.NetworkError
        is UnknownError -> DataSourceError.HttpError.UnknownError
    }
}

fun HttpStatusCode.toHttpException(): HttpException {
    return when (this) {
        HttpStatusCode.BadRequest -> HttpException.BadRequest()
        HttpStatusCode.Unauthorized -> HttpException.Unauthorized()
        HttpStatusCode.Forbidden -> HttpException.Forbidden()
        HttpStatusCode.NotFound -> HttpException.NotFound()
        else -> {
            if (value in 500..599) {
                HttpException.ServerError(statusCode = "$value")
            } else {
                HttpException.UnknownHttpRemoteError(statusCode = "$value")
            }
        }
    }
}

fun Exception.toHttpException(): HttpException {
    return when (this) {
        is SerializationException -> HttpException.SerializationError(message = "$cause: $message")
        is HttpRequestTimeoutException -> HttpException.Timeout(message = "$cause: $message")
        is ConnectException -> HttpException.NetworkError(message = "$cause: $message")
        is UnknownHostException -> HttpException.NetworkError(message = "$cause: $message")
        is IOException -> HttpException.NetworkError(message = "$cause: $message")
        else -> HttpException.UnknownError(message = "$cause: $message")
    }
}
