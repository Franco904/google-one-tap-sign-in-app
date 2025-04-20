package com.example.one_tap_sign_in.core.application.di.modules

import com.example.one_tap_sign_in.core.application.di.qualifiers.encryptedPreferencesQualifier
import com.example.one_tap_sign_in.core.application.di.qualifiers.plainPreferencesQualifier
import com.example.one_tap_sign_in.core.data.repositories.UserRepositoryImpl
import com.example.one_tap_sign_in.core.domain.repositories.UserRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<UserRepository> {
        UserRepositoryImpl(
            userApi = get(),
            encryptedPreferencesStorage = get(encryptedPreferencesQualifier),
            plainPreferencesStorage = get(plainPreferencesQualifier),
        )
    }
}
