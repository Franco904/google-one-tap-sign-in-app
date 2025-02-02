package com.example.core.domain.utils

import core.domain.utils.Error

sealed interface ValidationError : Error {
    enum class UserSession : ValidationError {
        IdTokenIsBlank,
        SessionIsNull,
        SessionIdIsBlank,
    }

    enum class User : ValidationError {
        NameIsBlank,
        NameLengthIsGreaterThan35Chars,
    }
}
