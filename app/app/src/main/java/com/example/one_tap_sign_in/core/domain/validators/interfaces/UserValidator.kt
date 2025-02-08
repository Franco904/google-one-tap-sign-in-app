package com.example.one_tap_sign_in.core.domain.validators.interfaces

import com.example.one_tap_sign_in.core.domain.models.User
import com.example.one_tap_sign_in.core.domain.utils.Result
import com.example.one_tap_sign_in.core.domain.utils.ResultCompound
import com.example.one_tap_sign_in.core.domain.utils.ValidationError

interface UserValidator {
    fun validate(user: User): ResultCompound<Unit, ValidationError>

    fun validateName(name: String?): Result<Unit, ValidationError.UserName>
}
