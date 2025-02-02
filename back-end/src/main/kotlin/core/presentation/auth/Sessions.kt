package com.example.core.presentation.auth

import com.example.core.data.constants.SESSION_COOKIE_NAME
import com.example.core.presentation.auth.models.UserSessionDto
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import java.io.File
import kotlin.time.Duration.Companion.seconds

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
