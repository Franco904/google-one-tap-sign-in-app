package com.example.one_tap_sign_in.core.domain.utils

typealias RootError = Error

sealed interface Result<out D, out E : RootError> {
    data class Success<out D, out E : RootError>(val data: D) : Result<D, E>
    data class Error<out D, out E : RootError>(val error: E) : Result<D, E>
}

fun <D, E : RootError> Result<D, E>.onSuccess(
    block: (D) -> Unit,
) = apply {
    if (this is Result.Success<D, E>) {
        block(data)
    }
}

suspend fun <D, E : RootError> Result<D, E>.onSuccessAsync(
    block: suspend (D) -> Unit,
) = apply {
    if (this is Result.Success<D, E>) {
        block(data)
    }
}

fun <D, E : RootError> Result<D, E>.onError(
    block: (E) -> Unit,
) = apply {
    if (this is Result.Error<D, E>) {
        block(error)
    }
}

suspend fun <D, E : RootError> Result<D, E>.onErrorAsync(
    block: suspend (E) -> Unit,
) = apply {
    if (this is Result.Error<D, E>) {
        block(error)
    }
}
