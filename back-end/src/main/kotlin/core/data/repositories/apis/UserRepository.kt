package com.example.core.data.repositories.apis

import com.example.core.data.entities.UserEntity
import org.bson.types.ObjectId

interface UserRepository {
    suspend fun findById(objectId: ObjectId): UserEntity?
}
