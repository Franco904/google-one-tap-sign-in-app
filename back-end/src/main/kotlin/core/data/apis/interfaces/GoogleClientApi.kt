package com.example.core.data.apis.interfaces

import com.example.core.data.apiModels.UserCredentials

interface GoogleClientApi {
    fun verifyIdToken(idToken: String): UserCredentials?
}
