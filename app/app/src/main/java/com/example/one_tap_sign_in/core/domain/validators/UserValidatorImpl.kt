package com.example.one_tap_sign_in.core.domain.validators

import com.example.one_tap_sign_in.core.data.models.User
import com.example.one_tap_sign_in.core.domain.utils.Result
import com.example.one_tap_sign_in.core.domain.utils.ResultCompound
import com.example.one_tap_sign_in.core.domain.utils.ValidationError
import com.example.one_tap_sign_in.core.domain.utils.onError
import com.example.one_tap_sign_in.core.domain.validators.interfaces.UserValidator

class UserValidatorImpl : UserValidator {
    override fun validate(user: User): ResultCompound<Unit, ValidationError.UserName> {
        val errors = mutableListOf<ValidationError.UserName>()

        validateName(user.name).onError { nameError -> errors.add(nameError) }

        return if (errors.isEmpty()) {
            ResultCompound.Success(Unit)
        } else ResultCompound.ErrorList(errors.toList())
    }

    override fun validateName(name: String?): Result<Unit, ValidationError.UserName> {
        return when {
            name.isNullOrBlank() -> Result.Error(ValidationError.UserName.IsBlank)
            name.length > 35 -> Result.Error(ValidationError.UserName.LengthIsGreaterThan35Chars)
            else -> Result.Success(Unit)
        }
    }
}
