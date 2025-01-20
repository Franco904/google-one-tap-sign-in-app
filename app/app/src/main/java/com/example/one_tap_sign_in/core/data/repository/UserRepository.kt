package com.example.one_tap_sign_in.core.data.repository

interface UserRepository {
    suspend fun authenticateUser(idToken: String)

    suspend fun saveUserCredentials(
        displayName: String?,
        profilePictureUrl: String?,
    )

    suspend fun readUserCredentials(): Pair<String?, String?>

    suspend fun deleteUserCredentials()

    suspend fun saveIsSignedIn(isSignedIn: Boolean)

    suspend fun readIsSignedIn(): Boolean?
}
