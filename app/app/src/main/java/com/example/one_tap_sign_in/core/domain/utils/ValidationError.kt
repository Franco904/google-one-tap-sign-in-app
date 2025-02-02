package com.example.one_tap_sign_in.core.domain.utils

sealed interface ValidationError : Error {
    enum class UserName : ValidationError {
        IsBlank,
        LengthIsGreaterThan35Chars,
    }
}
