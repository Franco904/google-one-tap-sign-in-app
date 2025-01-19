package com.example.one_tap_sign_in.core.data.repository

interface UserRepository {
    suspend fun authenticateUser(idToken: String)

    suspend fun writeIsSignedIn(isSignedIn: Boolean)

    suspend fun isSignedIn(): Boolean?
}
