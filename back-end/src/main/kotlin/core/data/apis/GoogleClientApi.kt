package com.example.core.data.apis

import com.example.core.data.apiModels.UserCredentials
import com.example.core.data.apis.interfaces.GoogleClientApi
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier

class GoogleClientApiImpl(
    private val googleIdTokenVerifier: GoogleIdTokenVerifier,
) : GoogleClientApi {
    override fun verifyIdToken(idToken: String): UserCredentials? {
        val googleIdToken = googleIdTokenVerifier.verify(idToken)

        return if (googleIdToken == null) {
            null
        } else {
            UserCredentials.fromGoogleIdToken(googleIdToken)
        }
    }
}
