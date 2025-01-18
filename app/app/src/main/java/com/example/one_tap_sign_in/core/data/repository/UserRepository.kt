package com.example.one_tap_sign_in.core.data.repository

interface UserRepository {
    suspend fun writeIdToken(idToken: String)

    suspend fun getIdToken(): String?
}
