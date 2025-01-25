package com.example.core.data.apis.interfaces

import com.example.core.data.apis.models.UserCredentials

interface GoogleClientApi {
    fun verifyIdToken(idToken: String): UserCredentials
}
