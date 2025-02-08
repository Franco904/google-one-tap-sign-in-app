package com.example.one_tap_sign_in.core.data.dataSources.remoteBackend.responseDtos

import com.example.one_tap_sign_in.core.domain.models.User
import kotlinx.serialization.Serializable

@Serializable
data class GetUserResponseDto(
    val email: String,
    val name: String,
    val profilePictureUrl: String,
) {
    fun toUser(): User {
        return User(
            email = email,
            name = name,
            profilePictureUrl = profilePictureUrl,
        )
    }
}
