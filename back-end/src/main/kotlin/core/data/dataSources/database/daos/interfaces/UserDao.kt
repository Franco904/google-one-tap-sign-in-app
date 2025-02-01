package com.example.core.data.dataSources.database.daos.interfaces

import com.example.core.data.dataSources.database.entities.UserEntity

interface UserDao {
    suspend fun createOrIgnore(user: UserEntity): UserEntity

    suspend fun findById(id: String): UserEntity

    suspend fun update(user: UserEntity)

    suspend fun delete(id: String)
}
