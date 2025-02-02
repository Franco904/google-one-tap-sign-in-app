package com.example.core.data.dataSources.authServer.models

import com.example.core.domain.models.User
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken

data class UserCredentialsResponseDto(
    val sub: String,
    val email: String,
    val name: String,
    val profilePictureUrl: String,
) {
    fun toUser(): User {
        return User(
            id = sub,
            email = email,
            name = name,
            profilePictureUrl = profilePictureUrl,
        )
    }

    companion object {
        fun fromGoogleIdToken(googleIdToken: GoogleIdToken): UserCredentialsResponseDto {
            return with(googleIdToken) {
                UserCredentialsResponseDto(
                    sub = payload["sub"].toString(),
                    email = payload.email,
                    name = payload["name"].toString(),
                    profilePictureUrl = payload["picture"].toString(),
                )
            }
        }
    }
}
