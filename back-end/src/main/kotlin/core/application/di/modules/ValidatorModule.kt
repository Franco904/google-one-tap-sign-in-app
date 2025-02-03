package com.example.core.application.di.modules

import com.example.core.domain.validators.UserSessionValidatorImpl
import com.example.core.domain.validators.UserValidatorImpl
import com.example.core.domain.validators.interfaces.UserSessionValidator
import com.example.core.domain.validators.interfaces.UserValidator
import org.koin.dsl.module

val validatorModule = module {
    single<UserSessionValidator> {
        UserSessionValidatorImpl()
    }

    single<UserValidator> {
        UserValidatorImpl()
    }
}
