package com.example.core.data.daos.apis

import com.example.core.data.entities.UserEntity
import org.bson.types.ObjectId

interface UserDao {
    suspend fun findById(objectId: ObjectId): UserEntity?
}
