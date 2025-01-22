package com.example.core.routing

import com.example.signIn.signInRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        signInRoutes()
    }
}
