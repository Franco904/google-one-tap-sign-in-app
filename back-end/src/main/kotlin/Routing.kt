package com.example

import com.example.signIn.signInRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        rootRoute()

        signInRoutes()
    }
}

fun Route.rootRoute() {
    get("/") {
        call.respondText("Hello World!")
    }
}
