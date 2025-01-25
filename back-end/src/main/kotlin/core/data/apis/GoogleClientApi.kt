package com.example.core.data.apis

import com.example.core.data.apis.interfaces.GoogleClientApi
import com.example.core.data.apis.models.UserCredentials
import com.example.core.exceptionHandling.exceptions.InvalidTokenException
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier

class GoogleClientApiImpl(
    private val googleIdTokenVerifier: GoogleIdTokenVerifier,
) : GoogleClientApi {
    override fun verifyIdToken(idToken: String): UserCredentials {
        val googleIdToken = googleIdTokenVerifier.verify(idToken)

        if (googleIdToken == null) throw InvalidTokenException()

        return UserCredentials.fromGoogleIdToken(googleIdToken)
    }
}
