package com.example.core.security

import com.example.core.constants.SESSION_NAME
import com.example.core.security.session.UserSession
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import java.io.File
import kotlin.time.Duration.Companion.minutes

fun Application.configureSessions() {
    install(Sessions) {
        cookie<UserSession>(
            name = SESSION_NAME,
            storage = directorySessionStorage(File(".sessions")),
        ) {
            cookie.apply {
                path = "/"
                sameSite = SameSite.Strict // Cookie is only send for the same user context (prevent CSRF attacks)
                httpOnly = true // Prevents JavaScript access (prevent XSS attacks)
                maxAge = 30.minutes
            }
        }
    }
}
