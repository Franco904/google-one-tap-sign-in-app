package com.example.core.presentation.auth

import com.example.core.data.constants.SESSION_COOKIE_NAME
import com.example.core.presentation.auth.models.UserSession
import com.example.core.presentation.exceptionHandling.exceptions.SessionExpiredException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import java.io.File

fun Application.configureAuthentication() {
    install(Authentication) {
        session<UserSession>(SESSION_COOKIE_NAME) {
            validate { session ->
                val isSessionExpired = System.currentTimeMillis() >= session.expirationTimestamp

                if (isSessionExpired) {
                    val sessionFolder = File(".sessions")
                    val sessionFiles = sessionFolder.listFiles()

                    sessionFiles?.forEach { it.deleteRecursively() }

                    return@validate null
                }

                return@validate session
            }

            challenge {
                throw SessionExpiredException()
            }
        }
    }
}
