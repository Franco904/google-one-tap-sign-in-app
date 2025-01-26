package com.example.one_tap_sign_in.core.data.repositories

interface UserRepository {
    suspend fun isSignedIn(): Boolean

    suspend fun signInUser(
        idToken: String,
        displayName: String?,
        profilePictureUrl: String?,
    )

    suspend fun readUserCredentials(): Pair<String?, String?>

    suspend fun signOutUser()
}
