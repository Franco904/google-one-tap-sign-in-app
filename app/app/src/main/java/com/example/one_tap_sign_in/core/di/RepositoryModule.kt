package com.example.one_tap_sign_in.core.di

import com.example.one_tap_sign_in.core.data.repositories.UserRepositoryImpl
import com.example.one_tap_sign_in.core.domain.repositories.UserRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<UserRepository> {
        UserRepositoryImpl(
            userApi = get(),
            userPreferencesStorage = get(),
        )
    }
}
