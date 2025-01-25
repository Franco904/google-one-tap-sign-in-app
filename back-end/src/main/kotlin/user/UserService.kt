package com.example.user

import com.example.core.data.repositories.interfaces.UserRepository
import com.example.core.exceptionHandling.exceptions.BadRequestException
import com.example.core.security.session.interfaces.SessionProvider

class UserService(
    private val userRepository: UserRepository,
    private val sessionProvider: SessionProvider,
) {
    suspend fun startSession(idToken: String): String {
        if (idToken.isBlank()) throw BadRequestException("Id token is blank.")

        val user = userRepository.verifyIdToken(idToken)
        val sessionId = sessionProvider.generateSessionId(user)

        return sessionId
    }
}
