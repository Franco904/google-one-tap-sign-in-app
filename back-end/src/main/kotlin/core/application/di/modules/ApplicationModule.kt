package com.example.core.application.di.modules

import io.ktor.server.application.*
import org.koin.dsl.module

fun Application.applicationModule() = module {
    single<Application> { this@applicationModule }
}
