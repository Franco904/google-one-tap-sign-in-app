package com.example.core.data.dataSources.authServer

import com.example.core.data.dataSources.authServer.interfaces.AuthClientApi
import com.example.core.data.dataSources.authServer.models.UserCredentialsResponseDto
import com.example.core.presentation.exceptionHandling.exceptions.InvalidTokenException
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier

class GoogleClientApiImpl(
    private val googleIdTokenVerifier: GoogleIdTokenVerifier,
) : AuthClientApi {
    override fun verifyIdToken(idToken: String): UserCredentialsResponseDto {
        val googleIdToken = googleIdTokenVerifier.verify(idToken)

        if (googleIdToken == null) throw InvalidTokenException()

        return UserCredentialsResponseDto.fromGoogleIdToken(googleIdToken)
    }
}
