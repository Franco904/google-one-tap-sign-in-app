package com.example.core.security

import com.example.core.exceptionHandling.exceptions.UnauthorizedException
import com.example.core.security.session.UserSession
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureAuthentication() {
    install(Authentication) {
        session<UserSession>(name = "auth-session") {
            validate { session ->
                session
            }
            challenge {
                throw UnauthorizedException()
            }
        }
    }
}
