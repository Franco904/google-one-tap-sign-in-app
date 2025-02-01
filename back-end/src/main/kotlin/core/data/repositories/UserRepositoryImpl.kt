package com.example.core.data.repositories

import com.example.core.data.dataSources.authServer.interfaces.AuthClientApi
import com.example.core.data.dataSources.database.daos.interfaces.UserDao
import com.example.core.data.dataSources.database.entities.UserEntity
import com.example.core.domain.repositories.UserRepository

class UserRepositoryImpl(
    private val userDao: UserDao,
    private val authClientApi: AuthClientApi,
) : UserRepository {
    override suspend fun verifyIdToken(idToken: String): UserEntity {
        val userCredentials = authClientApi.verifyIdToken(idToken)

        val userEntity = userDao.createOrIgnore(
            user = UserEntity.fromUserCredentials(userCredentials)
        )

        return userEntity
    }

    override suspend fun getUser(userId: String): UserEntity {
        return userDao.findById(id = userId)
    }

    override suspend fun updateUserName(
        userId: String,
        newName: String,
    ) {
        val user = userDao.findById(id = userId)
        val updatedUser = user.copy(name = newName)

        userDao.update(user = updatedUser)
    }

    override suspend fun deleteUser(userId: String) {
        userDao.delete(id = userId)
    }
}
