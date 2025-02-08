package com.example.one_tap_sign_in.core.domain.utils

sealed interface ResultCompound<out D, out E : RootError> {
    data class Success<out D, out E : RootError>(val data: D) : ResultCompound<D, E>
    data class ErrorList<out D, out E : RootError>(val errors: List<E>) : ResultCompound<D, E>
}

inline fun <D, E : RootError> ResultCompound<D, E>.onSuccess(
    block: (D) -> Unit,
) = apply {
    if (this is ResultCompound.Success<D, E>) {
        block(data)
    }
}

inline fun <D, E : RootError> ResultCompound<D, E>.onErrors(
    block: (List<E>) -> Unit,
) = apply {
    if (this is ResultCompound.ErrorList<D, E>) {
        block(errors)
    }
}
