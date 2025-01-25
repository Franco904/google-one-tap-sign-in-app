package com.example.user

import com.example.core.data.entities.UserEntity
import com.example.core.data.repositories.interfaces.UserRepository
import com.example.core.exceptionHandling.exceptions.BlankTokenException
import com.example.core.exceptionHandling.exceptions.InvalidSessionException
import com.example.core.exceptionHandling.exceptions.UserNotFoundException
import com.example.core.security.session.UserSession

class UserService(
    private val userRepository: UserRepository,
) {
    suspend fun startSession(idToken: String): Pair<String, String> {
        if (idToken.isBlank()) throw BlankTokenException()

        val user = userRepository.verifyIdToken(idToken)

        return user.id to user.name
    }

    suspend fun getUserFromSession(session: UserSession?): UserEntity {
        if (session == null || session.id.isBlank()) {
            throw InvalidSessionException()
        }

        val user = userRepository.getUser(userId = session.id) ?: throw UserNotFoundException()

        return user
    }
}
