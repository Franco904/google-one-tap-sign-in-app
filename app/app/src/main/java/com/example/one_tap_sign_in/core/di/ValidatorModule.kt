package com.example.one_tap_sign_in.core.di

import com.example.one_tap_sign_in.core.domain.validators.UserValidatorImpl
import com.example.one_tap_sign_in.core.domain.validators.interfaces.UserValidator
import org.koin.dsl.module

val validatorModule = module {
    factory<UserValidator> {
        UserValidatorImpl()
    }
}
