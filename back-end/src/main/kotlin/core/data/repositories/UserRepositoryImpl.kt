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
            user = UserEntity.fromUserCredentials(userCredentials)
        )
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
