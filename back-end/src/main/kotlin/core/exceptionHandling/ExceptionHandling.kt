package com.example.core.exceptionHandling

import com.example.core.exceptionHandling.exceptions.BadRequestException
import com.example.core.exceptionHandling.exceptions.UnauthorizedException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureExceptionHandling() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(
                text = "[500] Internal server error: ${cause.message}",
                status = HttpStatusCode.InternalServerError,
            )
        }

        exception<BadRequestException> { call, cause ->
            call.respondText(
                text = "[400] Bad request error: ${cause.message}",
                status = HttpStatusCode.Unauthorized,
            )
        }

        exception<UnauthorizedException> { call, cause ->
            call.respondText(
                text = "[401] Unauthorized error: ${cause.message}",
                status = HttpStatusCode.Unauthorized,
            )
        }
    }
}
