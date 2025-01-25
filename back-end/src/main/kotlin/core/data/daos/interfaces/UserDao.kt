package com.example.core.data.daos.interfaces

import com.example.core.data.entities.UserEntity

interface UserDao {
    suspend fun create(
        email: String,
        name: String?,
        profilePictureUrl: String?,
    ): UserEntity

    suspend fun getByEmail(email: String): UserEntity?
}
