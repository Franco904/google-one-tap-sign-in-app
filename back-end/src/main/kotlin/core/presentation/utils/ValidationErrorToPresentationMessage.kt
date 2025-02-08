package com.example.core.presentation.utils

import com.example.core.domain.utils.ValidationError

fun ValidationError.toPresentationMessage() = when (this) {
    ValidationError.UserSession.IdTokenIsBlank -> "Id token is blank."
    ValidationError.UserSession.SessionIsNull -> "Session is null."
    ValidationError.UserSession.SessionIdIsBlank -> "Session id is blank."
    ValidationError.User.NameIsBlank -> "User name is blank."
    ValidationError.User.NameLengthIsGreaterThan35Chars -> "User name length must be up to 35 chars."
}
