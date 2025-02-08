package com.example.core.application.auth

import com.example.core.application.auth.models.UserSessionDto
import com.example.core.application.constants.SESSION_COOKIE_NAME
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import java.io.File

fun Application.configureSessions() {
    install(Sessions) {
        cookie<UserSessionDto>(
            name = SESSION_COOKIE_NAME,
            storage = directorySessionStorage(File(".sessions")),
        ) {
            cookie.apply {
                path = "/"
                sameSite = SameSite.Strict // Cookie is only send for the same user context (prevent CSRF attacks)
                httpOnly = true // Prevents JavaScript access (prevent XSS attacks)
            }
        }
    }
}
