package com.example.core.exceptionHandling

import com.example.core.exceptionHandling.exceptions.UnauthorizedException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureExceptionHandling() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(
                text = "500: $cause",
                status = HttpStatusCode.InternalServerError,
            )
        }

        exception<UnauthorizedException> { call, cause ->
            call.respondText(
                text = "401: $cause",
                status = HttpStatusCode.Unauthorized,
            )
        }
    }
}
