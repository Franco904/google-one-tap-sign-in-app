package com.example.one_tap_sign_in.core.domain.repositories

import com.example.one_tap_sign_in.core.domain.models.User
import com.example.one_tap_sign_in.core.domain.utils.DataSourceError
import com.example.one_tap_sign_in.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun isUserSignedIn(): Result<Boolean, DataSourceError>

    suspend fun didUserExplicitlySignOut(): Result<Boolean, DataSourceError>

    suspend fun signInUser(
        idToken: String,
        displayName: String?,
        profilePictureUrl: String?,
    ): Result<Unit, DataSourceError>

    fun watchUser(): Flow<Result<User, DataSourceError>>

    suspend fun updateUser(newName: String): Result<Unit, DataSourceError>

    suspend fun retryUpdateUser()

    suspend fun deleteUser(): Result<Unit, DataSourceError>

    suspend fun signOutUser(): Result<Unit, DataSourceError>
}
