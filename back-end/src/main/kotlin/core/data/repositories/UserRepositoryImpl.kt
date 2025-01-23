package com.example.core.data.repositories

import com.example.core.data.daos.apis.UserDao
import com.example.core.data.entities.UserEntity
import com.example.core.data.repositories.apis.UserRepository
import org.bson.types.ObjectId

class UserRepositoryImpl(
    private val userDao: UserDao,
) : UserRepository {
    override suspend fun findById(objectId: ObjectId): UserEntity? {
        TODO("Not yet implemented")
    }
}
