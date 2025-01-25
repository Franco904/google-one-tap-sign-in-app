package com.example.core.security

import com.example.core.constants.SESSION_NAME
import com.example.core.exceptionHandling.exceptions.UnauthorizedException
import com.example.core.security.session.UserSession
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureAuthentication() {
    install(Authentication) {
        session<UserSession>(SESSION_NAME) {
            validate { session ->
                session
            }

            challenge {
                throw UnauthorizedException()
            }
        }
    }
}
