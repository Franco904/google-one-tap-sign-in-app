package com.example

import com.example.core.di.configureDependencyInjection
import com.example.core.exceptionHandling.configureExceptionHandling
import com.example.core.monitoring.configureMonitoring
import com.example.core.security.configureSecurity
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureRouting()

    configureExceptionHandling()
    configureDependencyInjection()
    configureSecurity()
    configureMonitoring()
}
