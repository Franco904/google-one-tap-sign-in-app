package com.example.one_tap_sign_in.core.data.repositories

import com.example.one_tap_sign_in.core.data.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun isSignedIn(): Boolean

    suspend fun signInUser(
        idToken: String,
        displayName: String?,
        profilePictureUrl: String?,
    )

    suspend fun getUser(): Flow<User>

    suspend fun updateUser(newName: String)

    suspend fun deleteUser()

    suspend fun signOutUser()
}
