package com.example.core.di

import com.example.core.di.modules.*
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDependencyInjection() {
    install(Koin) {
        slf4jLogger()

        modules(
            applicationModule(),
            serviceModule,
            repositoryModule,
            daoModule,
            databaseModule,
            webApiModule,
            monitoringModule,
            securityModule,
        )
    }
}
