package com.example.one_tap_sign_in.core.di

import com.example.one_tap_sign_in.core.data.repository.UserRepository
import com.example.one_tap_sign_in.core.data.repository.UserRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<UserRepository> {
        UserRepositoryImpl(
            userApi = get(),
            userPreferencesStorage = get(),
        )
    }
}
