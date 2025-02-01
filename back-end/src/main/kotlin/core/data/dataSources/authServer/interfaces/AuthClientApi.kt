package com.example.core.data.dataSources.authServer.interfaces

import com.example.core.data.dataSources.authServer.models.UserCredentialsResponseDto

interface AuthClientApi {
    fun verifyIdToken(idToken: String): UserCredentialsResponseDto
}
