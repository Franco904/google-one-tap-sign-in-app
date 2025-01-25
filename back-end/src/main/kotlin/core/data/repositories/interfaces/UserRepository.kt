package com.example.core.data.repositories.interfaces

import com.example.core.data.entities.UserEntity

interface UserRepository {
    suspend fun verifyIdToken(idToken: String): UserEntity
}
