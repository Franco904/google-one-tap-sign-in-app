package com.example.user

import com.example.core.domain.repositories.UserRepository
import com.example.core.domain.validators.interfaces.UserSessionValidator
import com.example.core.domain.validators.interfaces.UserValidator
import com.example.core.application.auth.models.UserSessionDto
import com.example.user.requestDtos.UpdateUserRequestDto
import com.example.user.responseDtos.GetUserResponseDto

class UserService(
    private val userSessionValidator: UserSessionValidator,
    private val userValidator: UserValidator,
    private val userRepository: UserRepository,
) {
    suspend fun createUserSession(idToken: String): UserSessionDto {
        userSessionValidator.validateIdToken(idToken)

        val user = userRepository.verifyIdToken(idToken)

        return UserSessionDto(
            id = user.id!!,
            name = user.name!!,
        )
    }

    suspend fun getUser(userSessionDto: UserSessionDto?): GetUserResponseDto {
        val userSession = userSessionDto?.toUserSession()
        userSessionValidator.validateUserSession(userSession = userSession)

        val user = userRepository.getUser(userId = userSession?.id!!)

        return GetUserResponseDto(
            email = user.email!!,
            name = user.name!!,
            profilePictureUrl = user.profilePictureUrl!!,
        )
    }

    suspend fun updateUser(
        userSessionDto: UserSessionDto?,
        userDto: UpdateUserRequestDto,
    ) {
        val userSession = userSessionDto?.toUserSession()
        userSessionValidator.validateUserSession(userSession = userSession)

        val user = userDto.toUser()
        userValidator.validate(user = user)

        userRepository.updateUserName(
            userId = userSession?.id!!,
            newName = user.name!!,
        )
    }

    suspend fun deleteUser(userSessionDto: UserSessionDto?) {
        val userSession = userSessionDto?.toUserSession()
        userSessionValidator.validateUserSession(userSession = userSession)

        userRepository.deleteUser(userId = userSession?.id!!)
    }
}
