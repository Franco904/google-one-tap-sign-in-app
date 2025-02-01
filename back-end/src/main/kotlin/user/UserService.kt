package com.example.user

import com.example.core.data.dataSources.database.entities.UserEntity
import com.example.core.domain.repositories.UserRepository
import com.example.core.presentation.auth.models.UserSession
import com.example.core.presentation.exceptionHandling.exceptions.BlankTokenException
import com.example.core.presentation.exceptionHandling.exceptions.InvalidSessionException
import com.example.user.requestDtos.UpdateUserRequestDto

class UserService(
    private val userRepository: UserRepository,
) {
    suspend fun createUserSession(idToken: String): UserSession {
        if (idToken.isBlank()) throw BlankTokenException()

        val user = userRepository.verifyIdToken(idToken)

        return UserSession(
            id = user.id,
            name = user.name,
        )
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
