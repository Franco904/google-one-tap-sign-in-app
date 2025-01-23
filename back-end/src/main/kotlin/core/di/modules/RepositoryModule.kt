package com.example.core.di.modules

import com.example.core.data.repositories.UserRepositoryImpl
import com.example.core.data.repositories.apis.UserRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<UserRepository> {
        UserRepositoryImpl(
            userDao = get(),
        )
    }
}
