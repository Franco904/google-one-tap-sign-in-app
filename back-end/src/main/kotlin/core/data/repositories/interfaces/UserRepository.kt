package com.example.core.data.repositories.interfaces

import com.example.core.data.entities.UserEntity

interface UserRepository {
    suspend fun verifyIdToken(idToken: String): UserEntity

    suspend fun getUser(userId: String): UserEntity

    suspend fun updateUserName(
        userId: String,
        newName: String,
    )

    suspend fun deleteUser(userId: String)
}
