package com.example.core.domain.repositories

import com.example.core.domain.models.User

interface UserRepository {
    suspend fun verifyIdToken(idToken: String): User

    suspend fun getUser(userId: String): User

    suspend fun updateUserName(
        userId: String,
        newName: String,
    )

    suspend fun deleteUser(userId: String)
}
