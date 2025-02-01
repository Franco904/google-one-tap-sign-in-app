package com.example.core.data.dataSources.authServer.models

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken

data class UserCredentials(
    val sub: String,
    val email: String,
    val name: String,
    val profilePictureUrl: String,
) {
    companion object {
        fun fromGoogleIdToken(googleIdToken: GoogleIdToken): UserCredentials {
            return with(googleIdToken) {
                UserCredentials(
                    sub = payload["sub"].toString(),
                    email = payload.email,
                    name = payload["name"].toString(),
                    profilePictureUrl = payload["picture"].toString(),
                )
            }
        }
    }
}
