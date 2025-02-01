package com.example.core.data.dataSources.database.entities

import com.example.core.domain.models.User
import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    val id: String,
    val email: String,
    val name: String,
    val profilePictureUrl: String,
) {
    fun toUser(): User {
        return User(
            id = id,
            email = email,
            name = name,
            profilePictureUrl = profilePictureUrl,
        )
    }

    companion object {
        fun fromUser(user: User): UserEntity {
            return UserEntity(
                id = user.id,
                email = user.email,
                name = user.name,
                profilePictureUrl = user.profilePictureUrl,
            )
        }
    }
}
