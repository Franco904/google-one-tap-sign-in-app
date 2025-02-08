package com.example.core.data.dataSources.authServer

import com.example.core.data.dataSources.authServer.interfaces.GoogleClientApi
import com.example.core.data.dataSources.authServer.models.UserCredentialsResponseDto
import com.example.core.presentation.exceptionHandling.exceptions.UserCredentialNotFoundException
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GoogleClientApiImpl(
    private val googleIdTokenVerifier: GoogleIdTokenVerifier,
) : GoogleClientApi {
    override suspend fun verifyIdToken(idToken: String): UserCredentialsResponseDto {
        return withContext(Dispatchers.IO) {
            val googleIdToken = googleIdTokenVerifier.verify(idToken)

            if (googleIdToken == null) throw UserCredentialNotFoundException()

            UserCredentialsResponseDto.fromGoogleIdToken(googleIdToken)
        }
    }
}
