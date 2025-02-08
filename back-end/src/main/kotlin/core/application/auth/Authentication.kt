package com.example.core.application.auth

import com.example.core.application.auth.models.UserSessionDto
import com.example.core.data.constants.SESSION_COOKIE_NAME
import com.example.core.presentation.exceptionHandling.exceptions.SessionExpiredException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

fun Application.configureAuthentication() {
    install(Authentication) {
        session<UserSessionDto>(SESSION_COOKIE_NAME) {
            validate { session ->
                val isSessionExpired = System.currentTimeMillis() >= session.expirationTimestamp

                if (isSessionExpired) {
                    deleteCurrentSessionFile(session = session)

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

private suspend fun deleteCurrentSessionFile(
    session: UserSessionDto,
) = withContext(Dispatchers.IO) {
    val sessionsMainFolder = File(".sessions")

    sessionsMainFolder.listFiles().forEach { sessionFolder ->
        sessionFolder.walkBottomUp().forEach { file ->
            if (file.isFile && file.readText().contains(session.id)) {
                sessionFolder.deleteRecursively()

                return@withContext
            }
        }
    }
}
