package com.example.core.data.apiModels

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken

data class UserCredentials(
    val email: String,
    val name: String?,
    val profilePictureUrl: String?,
) {
    companion object {
        fun fromGoogleIdToken(googleIdToken: GoogleIdToken): UserCredentials {
            return with(googleIdToken) {
                UserCredentials(
                    email = payload.email,
                    name = payload["name"]?.toString(),
                    profilePictureUrl = payload["picture"]?.toString(),
                )
            }
        }
    }
}
