package com.example.core.data.repositories

import com.example.core.data.dataSources.authServer.interfaces.AuthClientApi
import com.example.core.data.dataSources.database.daos.interfaces.UserDao
import com.example.core.data.dataSources.database.entities.UserEntity
import com.example.core.domain.models.User
import com.example.core.domain.repositories.UserRepository

class UserRepositoryImpl(
    private val userDao: UserDao,
    private val authClientApi: AuthClientApi,
) : UserRepository {
    override suspend fun verifyIdToken(idToken: String): User {
        val credentialsResponseDto = authClientApi.verifyIdToken(idToken)
        val userFromCredentials = credentialsResponseDto.toUser()

        val userEntity = userDao.createOrIgnore(
            user = UserEntity.fromUser(userFromCredentials)
        )

        return userEntity.toUser()
    }

    override suspend fun getUser(userId: String): User {
        return userDao.findById(id = userId).toUser()
    }

    override suspend fun updateUserName(
        userId: String,
        newName: String,
    ) {
        val userEntity = userDao.findById(id = userId)
        val updatedUserEntity = userEntity.copy(name = newName)

        userDao.update(user = updatedUserEntity)
    }

    override suspend fun deleteUser(userId: String) {
        userDao.delete(id = userId)
    }
}
