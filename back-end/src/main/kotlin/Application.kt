package com.example

import com.example.core.application.auth.configureAuthentication
import com.example.core.application.auth.configureSessions
import com.example.core.application.di.configureDependencyInjection
import com.example.core.application.monitoring.configureMonitoring
import com.example.core.presentation.contentNegotiation.configureContentNegotiation
import com.example.core.presentation.exceptionHandling.configureExceptionHandling
import com.example.core.presentation.routing.configureRouting
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    // Application
    configureDependencyInjection()
    configureSessions()
    configureAuthentication()
    configureMonitoring()

    // Presentation
    configureRouting()
    configureContentNegotiation()
    configureExceptionHandling()
}
