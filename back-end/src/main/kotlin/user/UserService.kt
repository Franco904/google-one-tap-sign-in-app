package com.example.user

import com.example.core.data.repositories.interfaces.UserRepository
import com.example.core.exceptionHandling.exceptions.BlankTokenException

class UserService(
    private val userRepository: UserRepository,
) {
    suspend fun startSession(idToken: String): Pair<String, String> {
        if (idToken.isBlank()) throw BlankTokenException()

        val user = userRepository.verifyIdToken(idToken)

        return user.id to user.name
    }
}
