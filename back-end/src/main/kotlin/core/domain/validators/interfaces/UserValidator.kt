package com.example.core.domain.validators.interfaces

import com.example.core.domain.models.User

interface UserValidator {
    fun validate(user: User)
}
