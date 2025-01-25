package com.example

import com.example.foo.fooRoutes
import com.example.user.UserService
import com.example.user.userRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.slf4j.Logger

fun Application.configureRouting() {
    val userService by inject<UserService>()

    val logger by inject<Logger>()

    routing {
        rootRoute()

        fooRoutes()
        userRoutes(userService, logger)
    }
}

fun Route.rootRoute() {
    get("/") {
        call.respondText("Hello World!")
    }
}
