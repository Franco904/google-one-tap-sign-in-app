package com.example.user

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

const val USER_ROUTE = "/user"

fun Route.userRoutes() {
    val userService by inject<UserService>()

    route(USER_ROUTE) {
        get("/sign-in") {
            call.respondText("true", status = HttpStatusCode.OK)
        }
    }
}
