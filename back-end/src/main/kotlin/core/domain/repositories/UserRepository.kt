package com.example.core.domain.repositories

import com.example.core.data.dataSources.database.entities.UserEntity

interface UserRepository {
    suspend fun verifyIdToken(idToken: String): UserEntity

    suspend fun getUser(userId: String): UserEntity

    suspend fun updateUserName(
        userId: String,
        newName: String,
    )

    suspend fun deleteUser(userId: String)
}
