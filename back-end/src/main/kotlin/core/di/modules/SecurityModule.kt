package com.example.core.di.modules

import com.example.core.security.session.SessionProviderImpl
import com.example.core.security.session.interfaces.SessionProvider
import org.koin.dsl.module

val securityModule = module {
    single<SessionProvider> {
        SessionProviderImpl()
    }
}
