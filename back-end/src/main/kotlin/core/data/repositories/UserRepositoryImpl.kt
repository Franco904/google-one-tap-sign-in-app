package com.example.core.data.repositories

import com.example.core.data.apis.interfaces.GoogleClientApi
import com.example.core.data.daos.interfaces.UserDao
import com.example.core.data.entities.UserEntity
import com.example.core.data.repositories.interfaces.UserRepository
import com.example.core.exceptionHandling.exceptions.BadRequestException

class UserRepositoryImpl(
    private val userDao: UserDao,
    private val googleClientApi: GoogleClientApi,
) : UserRepository {
    override suspend fun verifyIdToken(idToken: String): UserEntity {
        val userCredentials = googleClientApi.verifyIdToken(idToken)
            ?: throw BadRequestException("Invalid id token.")

        val user = userDao.getByEmail(userCredentials.email) ?: userDao.create(
            email = userCredentials.email,
            name = userCredentials.name,
            profilePictureUrl = userCredentials.profilePictureUrl,
        )

        return user
    }
}
