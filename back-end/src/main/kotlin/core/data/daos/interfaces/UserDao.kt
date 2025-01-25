package com.example.core.data.daos.interfaces

import com.example.core.data.entities.UserEntity

interface UserDao {
    suspend fun createOrIgnore(user: UserEntity): UserEntity

    suspend fun findById(id: String): UserEntity?

    suspend fun update(user: UserEntity): UserEntity

    suspend fun delete(id: String): String?
}
