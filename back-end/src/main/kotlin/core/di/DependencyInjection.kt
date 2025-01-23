package com.example.core.di

import com.example.core.di.modules.daoModule
import com.example.core.di.modules.databaseModule
import com.example.core.di.modules.repositoryModule
import com.example.core.di.modules.serviceModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDependencyInjection() {
    install(Koin) {
        slf4jLogger()

        modules(
            serviceModule,
            repositoryModule,
            daoModule,
            databaseModule,
        )
    }
}
