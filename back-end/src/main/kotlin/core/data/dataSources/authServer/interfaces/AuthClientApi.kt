package com.example.core.data.dataSources.authServer.interfaces

import com.example.core.data.dataSources.authServer.models.UserCredentials

interface AuthClientApi {
    fun verifyIdToken(idToken: String): UserCredentials
}
