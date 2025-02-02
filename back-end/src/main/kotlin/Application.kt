package com.example

import com.example.core.di.configureDependencyInjection
import com.example.core.presentation.auth.configureAuthentication
import com.example.core.presentation.auth.configureSessions
import com.example.core.presentation.contentNegotiation.configureContentNegotiation
import com.example.core.presentation.exceptionHandling.configureExceptionHandling
import com.example.core.presentation.monitoring.configureMonitoring
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureDependencyInjection()

    configureSessions()
    configureAuthentication()

    configureRouting()
    configureContentNegotiation()
    configureExceptionHandling()
    configureMonitoring()
}
