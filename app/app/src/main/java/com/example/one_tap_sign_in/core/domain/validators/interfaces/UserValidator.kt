package com.example.one_tap_sign_in.core.domain.validators.interfaces

import com.example.one_tap_sign_in.core.domain.models.User
import com.example.one_tap_sign_in.core.domain.utils.AppResult
import com.example.one_tap_sign_in.core.domain.utils.AppResultCompound
import com.example.one_tap_sign_in.core.domain.utils.ValidationError

interface UserValidator {
    fun validate(user: User): AppResultCompound<Unit, ValidationError>
}
