package com.example.core.di.modules

import com.example.user.UserService
import org.koin.dsl.module

val serviceModule = module {
    single<UserService> {
        UserService(
            userRepository = get(),
            sessionProvider = get(),
        )
    }
}
