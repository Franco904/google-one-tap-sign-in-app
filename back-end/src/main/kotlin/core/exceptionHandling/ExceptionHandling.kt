package com.example.core.exceptionHandling

import com.example.core.exceptionHandling.exceptions.*
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
    exception<BlankTokenException> { call, cause ->
        logger.error("[Token Verification] - ${cause.message}")

        call.respondText(
            text = "[400] Bad request error: ${cause.message}",
            status = HttpStatusCode.BadRequest,
        )
    }

    exception<InvalidTokenException> { call, cause ->
        logger.error("[Token Verification] - ${cause.message}")

        call.respondText(
            text = "[400] Bad request error: ${cause.message}",
            status = HttpStatusCode.BadRequest,
        )
    }

    exception<InvalidSessionException> { call, cause ->
        logger.error("[Session verification] - ${cause.message}")

        call.respondText(
            text = "[401] Unauthorized error: User is unauthorized to access this resource.",
            status = HttpStatusCode.Unauthorized,
        )
    }

    exception<SessionExpiredException> { call, cause ->
        logger.error("[Resource Access] - ${cause.message}")

        call.respondText(
            text = "[401] Unauthorized error: ${cause.message}",
            status = HttpStatusCode.Unauthorized,
        )
    }

    exception<UserNotFoundException> { call, cause ->
        logger.error("[Get user] - ${cause.message}")

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
        logger.error("[Create User] - ${cause.message}")

        call.respondText(
            text = "[500] Internal server error: ${cause.message}",
            status = HttpStatusCode.InternalServerError,
        )
    }

    exception<UpdateUserFailedException> { call, cause ->
        logger.error("[Update User] - ${cause.message}")

        call.respondText(
            text = "[500] Internal server error: ${cause.message}",
            status = HttpStatusCode.InternalServerError,
        )
    }

    exception<DeleteUserFailedException> { call, cause ->
        logger.error("[Delete User] - ${cause.message}")

        call.respondText(
            text = "[500] Internal server error: ${cause.message}",
            status = HttpStatusCode.InternalServerError,
        )
    }

    exception<Throwable> { call, cause ->
        logger.error("[Unknown error] - ${cause.message}")

        call.respondText(
            text = "[500] Internal server error: ${cause.message}",
            status = HttpStatusCode.InternalServerError,
        )
    }
}
