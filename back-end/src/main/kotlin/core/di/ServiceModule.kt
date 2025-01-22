package com.example.core.di

import com.example.signIn.SignInService
import org.koin.dsl.module

val serviceModule = module {
    single<SignInService> {
        SignInService()
    }
}
