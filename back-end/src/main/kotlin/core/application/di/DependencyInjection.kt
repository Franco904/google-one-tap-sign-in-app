package com.example.core.application.di

import com.example.core.application.di.modules.*
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDependencyInjection() {
    install(Koin) {
        slf4jLogger()

        modules(
            applicationModule(),
            serviceModule,
            validatorModule,
            repositoryModule,
            daoModule,
            databaseModule,
            webApiModule,
            monitoringModule,
        )
    }
}
