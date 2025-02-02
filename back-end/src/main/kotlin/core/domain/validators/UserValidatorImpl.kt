package com.example.core.domain.validators

import com.example.core.domain.models.User
import com.example.core.domain.utils.ValidationError
import com.example.core.domain.validators.interfaces.UserValidator
import com.example.core.presentation.exceptionHandling.exceptions.InvalidUserException

class UserValidatorImpl : UserValidator {
    override fun validate(user: User) {
        validateName(name = user.name)
    }

    private fun validateName(name: String?) {
        when {
            name.isNullOrBlank() -> {
                throw InvalidUserException(error = ValidationError.User.NameIsBlank)
            }

            name.length > 35 -> {
                throw InvalidUserException(error = ValidationError.User.NameLengthIsGreaterThan35Chars)
            }
        }
    }
}
