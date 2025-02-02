package com.example.core.domain.validators

import com.example.core.domain.models.UserSession
import com.example.core.domain.utils.ValidationError
import com.example.core.domain.validators.interfaces.UserSessionValidator
import com.example.core.presentation.exceptionHandling.exceptions.InvalidIdTokenException
import com.example.core.presentation.exceptionHandling.exceptions.InvalidSessionException

class UserSessionValidatorImpl : UserSessionValidator {
    override fun validateIdToken(idToken: String?) {
        if (idToken.isNullOrBlank()) {
            throw InvalidIdTokenException(error = ValidationError.UserSession.IdTokenIsBlank)
        }
    }

    override fun validateUserSession(userSession: UserSession?) {
        when {
            userSession == null -> {
                throw InvalidSessionException(error = ValidationError.UserSession.SessionIsNull)
            }

            userSession.id.isBlank() -> {
                throw InvalidSessionException(error = ValidationError.UserSession.SessionIdIsBlank)
            }
        }
    }
}
