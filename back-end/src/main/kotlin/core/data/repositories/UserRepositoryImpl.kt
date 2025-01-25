package com.example.core.data.repositories

import com.example.core.data.apis.interfaces.GoogleClientApi
import com.example.core.data.daos.interfaces.UserDao
import com.example.core.data.entities.UserEntity
import com.example.core.data.repositories.interfaces.UserRepository

class UserRepositoryImpl(
    private val userDao: UserDao,
    private val googleClientApi: GoogleClientApi,
) : UserRepository {
    override suspend fun verifyIdToken(idToken: String): UserEntity {
        val userCredentials = googleClientApi.verifyIdToken(idToken)

        return userDao.createOrIgnore(
            user = UserEntity(
                id = userCredentials.sub,
                email = userCredentials.email,
                name = userCredentials.name,
                profilePictureUrl = userCredentials.profilePictureUrl,
            )
        )
    }
}
