package com.example.core.application.di.modules

import io.ktor.server.application.*
import org.koin.dsl.module
import org.slf4j.Logger

val monitoringModule = module {
    single<Logger> {
        val application = get<Application>()
        application.log
    }
}
