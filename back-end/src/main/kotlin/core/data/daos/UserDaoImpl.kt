package com.example.core.data.daos

import com.example.core.data.daos.interfaces.UserDao
import com.example.core.data.entities.UserEntity
import com.mongodb.kotlin.client.coroutine.MongoDatabase

class UserDaoImpl(
//    private val database: MongoDatabase,
) : UserDao {
    override suspend fun create(
        email: String,
        name: String?,
        profilePictureUrl: String?,
    ): UserEntity {
        return UserEntity(
            email = email,
            name = name,
            profilePictureUrl = profilePictureUrl,
        )
    }

    override suspend fun getByEmail(email: String): UserEntity? {
//        val user = database.getCollection<UserEntity>(USERS_COLLECTION)
//            .withDocumentClass<UserEntity>()
//            .find(eq("email", email))
//            .firstOrNull()

        return null
    }

    companion object {
        private const val USERS_COLLECTION = "users"
    }
}
