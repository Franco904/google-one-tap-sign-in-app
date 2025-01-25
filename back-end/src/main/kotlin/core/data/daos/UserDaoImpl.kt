package com.example.core.data.daos

import com.example.core.data.daos.interfaces.UserDao
import com.example.core.data.entities.UserEntity
import com.example.core.exceptionHandling.exceptions.CreateUserFailedException
import com.example.core.exceptionHandling.exceptions.DeleteUserFailedException
import com.example.core.exceptionHandling.exceptions.UpdateUserFailedException
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull

class UserDaoImpl(
    database: MongoDatabase,
) : UserDao {
    private val users = database.getCollection<UserEntity>(
        collectionName = USERS_COLLECTION_NAME,
    )

    override suspend fun createOrIgnore(user: UserEntity): UserEntity {
        val existingUser = findById(id = user.id)

        return existingUser ?: run {
            val success = users.insertOne(document = user).wasAcknowledged()

            if (!success) throw CreateUserFailedException()

            user
        }
    }

    override suspend fun findById(id: String): UserEntity? {
        return users
            .find<UserEntity>(filter = eq(UserEntity::id.name, id))
            .limit(1)
            .firstOrNull()
    }

    override suspend fun update(user: UserEntity): UserEntity {
        val success = users
            .replaceOne(
                filter = eq(UserEntity::id.name, user.id),
                replacement = user,
            )
            .wasAcknowledged()

        if (!success) throw UpdateUserFailedException()

        return user
    }

    override suspend fun delete(id: String): String? {
        val success = users
            .deleteOne(filter = eq(UserEntity::id.name, id)).wasAcknowledged()

        if (!success) throw DeleteUserFailedException()

        return id
    }

    companion object {
        private const val USERS_COLLECTION_NAME = "users"
    }
}
