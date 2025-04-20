package com.example.one_tap_sign_in.core.domain.repositories

import com.example.one_tap_sign_in.core.domain.models.User
import com.example.one_tap_sign_in.core.domain.utils.AppResult
import com.example.one_tap_sign_in.core.domain.utils.DataSourceError
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun isUserSignedIn(): AppResult<Boolean, DataSourceError>

    suspend fun didUserExplicitlySignOut(): AppResult<Boolean, DataSourceError>

    suspend fun signInUser(
        idToken: String,
        displayName: String?,
        profilePictureUrl: String?,
    ): AppResult<Unit, DataSourceError>

    fun watchUser(): Flow<AppResult<User, DataSourceError>>

    suspend fun updateUser(newName: String): AppResult<Unit, DataSourceError>

    suspend fun retrySendUserUpdate(): AppResult<Unit, DataSourceError>

    suspend fun deleteUser(): AppResult<Unit, DataSourceError>

    suspend fun signOutUser(): AppResult<Unit, DataSourceError>
}
