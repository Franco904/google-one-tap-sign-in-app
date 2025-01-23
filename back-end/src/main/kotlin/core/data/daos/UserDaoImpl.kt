package com.example.core.data.daos

import com.example.core.data.daos.apis.UserDao
import com.example.core.data.entities.UserEntity
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId

class UserDaoImpl(
    private val database: MongoDatabase
) : UserDao {
    override suspend fun findById(objectId: ObjectId): UserEntity? {
        val user = database.getCollection<UserEntity>(USERS_COLLECTION)
            .withDocumentClass<UserEntity>()
            .find(eq("_id", objectId))
            .firstOrNull()

        return user
    }

    companion object {
        private const val USERS_COLLECTION = "users"
    }
}
