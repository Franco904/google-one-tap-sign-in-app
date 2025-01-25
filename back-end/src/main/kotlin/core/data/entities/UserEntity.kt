package com.example.core.data.entities

import com.example.core.data.apis.models.UserCredentials
import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    val id: String,
    val email: String,
    val name: String,
    val profilePictureUrl: String,
) {
    companion object {
        fun fromUserCredentials(userCredentials: UserCredentials): UserEntity {
            return UserEntity(
                id = userCredentials.sub,
                email = userCredentials.email,
                name = userCredentials.name,
                profilePictureUrl = userCredentials.profilePictureUrl,
            )
        }
    }
}
