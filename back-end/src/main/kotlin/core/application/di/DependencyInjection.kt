package com.example.core.application.di

import com.example.core.application.di.modules.applicationModule
import com.example.core.application.di.modules.daoModule
import com.example.core.application.di.modules.databaseModule
import com.example.core.application.di.modules.monitoringModule
import com.example.core.application.di.modules.repositoryModule
import com.example.core.application.di.modules.serviceModule
import com.example.core.application.di.modules.validatorModule
import com.example.core.application.di.modules.webApiModule
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
