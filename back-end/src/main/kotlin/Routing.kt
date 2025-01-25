package com.example

import com.example.foo.fooRoutes
import com.example.user.UserService
import com.example.user.userRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userService by inject<UserService>()

    routing {
        rootRoute()

        fooRoutes()
        userRoutes(userService)
    }
}

fun Route.rootRoute() {
    get("/") {
        call.respondText("Hello World!")
    }
}
