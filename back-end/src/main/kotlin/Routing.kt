package com.example

import com.example.user.userRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        rootRoute()

        userRoutes()
    }
}

fun Route.rootRoute() {
    get("/") {
        call.respondText("Hello World!")
    }
}
