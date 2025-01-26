package com.example.user

import com.example.core.data.entities.UserEntity
import com.example.core.data.repositories.interfaces.UserRepository
import com.example.core.exceptionHandling.exceptions.BlankTokenException
import com.example.core.exceptionHandling.exceptions.InvalidSessionException
import com.example.core.security.session.UserSession
import com.example.user.requestDtos.UpdateUserRequestDto

class UserService(
    private val userRepository: UserRepository,
) {
    suspend fun startSession(idToken: String): Pair<String, String> {
        if (idToken.isBlank()) throw BlankTokenException()

        val user = userRepository.verifyIdToken(idToken)

        return user.id to user.name
    }

    suspend fun getUser(session: UserSession?): UserEntity {
        if (session == null || session.id.isBlank()) {
            throw InvalidSessionException()
        }

        return userRepository.getUser(userId = session.id)
    }

    suspend fun updateUser(
        session: UserSession?,
        user: UpdateUserRequestDto,
    ) {
        if (session == null || session.id.isBlank()) {
            throw InvalidSessionException()
        }

        userRepository.updateUserName(
            userId = session.id,
            newName = user.name,
        )
    }

    suspend fun deleteUser(session: UserSession?) {
        if (session == null || session.id.isBlank()) {
            throw InvalidSessionException()
        }

        userRepository.deleteUser(userId = session.id)
    }
}
