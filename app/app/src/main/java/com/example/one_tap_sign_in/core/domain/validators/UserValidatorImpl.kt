package com.example.one_tap_sign_in.core.domain.validators

import com.example.one_tap_sign_in.core.domain.models.User
import com.example.one_tap_sign_in.core.domain.utils.AppResult
import com.example.one_tap_sign_in.core.domain.utils.AppResultCompound
import com.example.one_tap_sign_in.core.domain.utils.ValidationError
import com.example.one_tap_sign_in.core.domain.utils.onError
import com.example.one_tap_sign_in.core.domain.validators.interfaces.UserValidator

class UserValidatorImpl : UserValidator {
    override fun validate(user: User): AppResultCompound<Unit, ValidationError.UserName> {
        val errors = mutableListOf<ValidationError.UserName>()

        validateName(user.name).onError { nameError -> errors.add(nameError) }

        return if (errors.isEmpty()) {
            AppResultCompound.Success(Unit)
        } else AppResultCompound.Errors(errors.toList())
    }

    private fun validateName(name: String?): AppResult<Unit, ValidationError.UserName> {
        return when {
            name.isNullOrBlank() -> AppResult.Error(ValidationError.UserName.IsBlank)
            name.length > 35 -> AppResult.Error(ValidationError.UserName.LengthIsGreaterThan35Chars)
            else -> AppResult.Success(Unit)
        }
    }
}
