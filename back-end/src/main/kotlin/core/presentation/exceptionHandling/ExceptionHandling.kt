package com.example.core.presentation.exceptionHandling

import com.example.core.presentation.exceptionHandling.exceptions.*
import com.example.core.presentation.utils.toResponseMessage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject
import org.slf4j.Logger

fun Application.configureExceptionHandling() {
    val logger by inject<Logger>()

    install(StatusPages) {
        configure400Responses(logger = logger)
        configure500Responses(logger = logger)
    }
}

fun StatusPagesConfig.configure400Responses(
    logger: Logger,
) {
    exception<InvalidIdTokenException> { call, cause ->
        logger.error("[Id token validation] - ${cause.message}: ${cause.error.toResponseMessage()}")

        call.respondText(
            text = "[400] Bad request error: ${cause.error} - ${cause.message}",
            status = HttpStatusCode.BadRequest,
        )
    }

    exception<InvalidSessionException> { call, cause ->
        logger.error("[Session validation] - ${cause.message}: ${cause.error.toResponseMessage()}")

        call.respondText(
            text = "[400] Bad request error: ${cause.error} - ${cause.message}",
            status = HttpStatusCode.BadRequest,
        )
    }

    exception<InvalidUserException> { call, cause ->
        logger.error("[User validation] - ${cause.message}: ${cause.error.toResponseMessage()}")

        call.respondText(
            text = "[400] Bad request error: ${cause.error} - ${cause.message}",
            status = HttpStatusCode.BadRequest,
        )
    }

    exception<UserCredentialNotFoundException> { call, cause ->
        logger.error("[Session auth] - ${cause.cause}: ${cause.message}")

        call.respondText(
            text = "[401] Unauthorized error: ${cause.message}",
            status = HttpStatusCode.Unauthorized,
        )
    }

    exception<SessionExpiredException> { call, cause ->
        logger.error("[Resource Access] - ${cause.cause}: ${cause.message}")

        call.respondText(
            text = "[401] Unauthorized error: ${cause.message}",
            status = HttpStatusCode.Unauthorized,
        )
    }

    exception<UserNotFoundException> { call, cause ->
        logger.error("[Get user] - ${cause.cause}: ${cause.message}")

        call.respondText(
            text = "[404] Resource not found error: ${cause.message}",
            status = HttpStatusCode.NotFound,
        )
    }
}

fun StatusPagesConfig.configure500Responses(
    logger: Logger,
) {
    exception<CreateUserFailedException> { call, cause ->
        logger.error("[Create User] - ${cause.cause}: ${cause.message}")

        call.respondText(
            text = "[500] Internal server error: ${cause.message}",
            status = HttpStatusCode.InternalServerError,
        )
    }

    exception<UpdateUserFailedException> { call, cause ->
        logger.error("[Update User] - ${cause.cause}: ${cause.message}")

        call.respondText(
            text = "[500] Internal server error: ${cause.message}",
            status = HttpStatusCode.InternalServerError,
        )
    }

    exception<DeleteUserFailedException> { call, cause ->
        logger.error("[Delete User] - ${cause.cause}: ${cause.message}")

        call.respondText(
            text = "[500] Internal server error: ${cause.message}",
            status = HttpStatusCode.InternalServerError,
        )
    }

    exception<Throwable> { call, cause ->
        logger.error("[Unknown error] - ${cause.cause}: ${cause.message}")

        call.respondText(
            text = "[500] Internal server error: ${cause.message}",
            status = HttpStatusCode.InternalServerError,
        )
    }
}
