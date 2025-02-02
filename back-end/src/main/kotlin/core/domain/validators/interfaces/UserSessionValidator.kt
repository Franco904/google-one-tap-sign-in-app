package com.example.core.domain.validators.interfaces

import com.example.core.domain.models.UserSession

interface UserSessionValidator {
    fun validateIdToken(idToken: String?)

    fun validateUserSession(userSession: UserSession?)
}
