package com.example.one_tap_sign_in.core.data.repositories

import android.util.Log
import com.example.one_tap_sign_in.core.data.dataSources.preferences.encrypted.EncryptedPreferences
import com.example.one_tap_sign_in.core.data.dataSources.preferences.interfaces.PreferencesStorage
import com.example.one_tap_sign_in.core.data.dataSources.preferences.plain.PlainPreferences
import com.example.one_tap_sign_in.core.data.dataSources.remoteBackend.apis.interfaces.UserApi
import com.example.one_tap_sign_in.core.data.dataSources.remoteBackend.requestDtos.SignInRequestDto
import com.example.one_tap_sign_in.core.data.dataSources.remoteBackend.requestDtos.UpdateUserRequestDto
import com.example.one_tap_sign_in.core.data.exceptions.PreferencesException
import com.example.one_tap_sign_in.core.data.exceptions.RemoteBackendException
import com.example.one_tap_sign_in.core.domain.models.User
import com.example.one_tap_sign_in.core.domain.repositories.UserRepository
import com.example.one_tap_sign_in.core.domain.utils.DataSourceError
import com.example.one_tap_sign_in.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val userApi: UserApi,
    private val encryptedPreferencesStorage: PreferencesStorage<EncryptedPreferences>,
    private val plainPreferencesStorage: PreferencesStorage<PlainPreferences>,
) : UserRepository {
    override suspend fun isUserSignedIn(): Result<Boolean, DataSourceError> {
        return try {
            val isSignedIn =
                encryptedPreferencesStorage.readPreferences().first().sessionCookie != null
            Result.Success(data = isSignedIn)
        } catch (e: Exception) {
            Log.e(TAG, "isUserSignedIn - ${e.message}")

            val error = when (e) {
                is PreferencesException -> e.toPreferencesError()
                else -> throw e
            }
            Result.Error(error = error)
        }
    }

    override suspend fun didUserExplicitlySignOut(): Result<Boolean, DataSourceError> {
        return try {
            val didUserExplicitlySignOut =
                plainPreferencesStorage.readPreferences()
                    .first().didUserExplicitlySignOut != false
            Result.Success(data = didUserExplicitlySignOut)
        } catch (e: Exception) {
            Log.e(TAG, "didUserExplicitlySignOut - ${e.message}")

            val error = when (e) {
                is PreferencesException -> e.toPreferencesError()
                else -> throw e
            }
            Result.Error(error = error)
        }
    }

    override suspend fun signInUser(
        idToken: String,
        displayName: String?,
        profilePictureUrl: String?,
    ): Result<Unit, DataSourceError> {
        return try {
            val requestDto = SignInRequestDto(idToken = idToken)
            userApi.signInUser(signInRequestDto = requestDto)

            encryptedPreferencesStorage.savePreferences { prefs ->
                prefs.copy(
                    displayName = displayName,
                    profilePictureUrl = profilePictureUrl,
                )
            }

            plainPreferencesStorage.savePreferences { prefs ->
                prefs.copy(didUserExplicitlySignOut = false)
            }

            Result.Success(data = Unit)
        } catch (e: Exception) {
            Log.e(TAG, "signInUser - ${e.message}")

            val error = when (e) {
                is PreferencesException -> e.toPreferencesError()
                is RemoteBackendException -> e.toRemoteBackendError()
                else -> throw e
            }
            Result.Error(error = error)
        }
    }

    override fun watchUser(): Flow<Result<User, DataSourceError>> = flow {
        // Read cached user data from preferences storage
        try {
            val preferences = encryptedPreferencesStorage.readPreferences().first()
            val userFromPreferences = preferences.toUser()

            emit(Result.Success(data = userFromPreferences))
        } catch (e: PreferencesException) {
            Log.e(TAG, "watchUser - ${e.message}")
            emit(Result.Error(error = e.toPreferencesError()))
        }

        // Fetch user data from remote backend API
        try {
            val responseDto = userApi.getUser()
            val userFromRemoteBackend = responseDto.toUser()

            // Cache the fetched data in preferences storage
            encryptedPreferencesStorage.savePreferences { prefs ->
                prefs.copy(
                    displayName = userFromRemoteBackend.name,
                    profilePictureUrl = userFromRemoteBackend.profilePictureUrl,
                )
            }

            emit(Result.Success(data = userFromRemoteBackend))
        } catch (e: Exception) {
            Log.e(TAG, "watchUser - ${e.message}")

            if (e is PreferencesException) {
                emit(Result.Error(error = e.toPreferencesError()))
            }
        }

        // Watch the cached user data for updates
        try {
            emitAll(
                encryptedPreferencesStorage.readPreferences()
                    .map { prefs: EncryptedPreferences ->
                        Result.Success<User, DataSourceError>(data = prefs.toUser())
                    }
                    .distinctUntilChanged()
            )
        } catch (e: PreferencesException) {
            Log.e(TAG, "watchUser - ${e.message}")
            emit(Result.Error(error = e.toPreferencesError()))
        }
    }

    override suspend fun updateUser(newName: String): Result<Unit, DataSourceError> {
        try {
            encryptedPreferencesStorage.savePreferences { prefs ->
                prefs.copy(displayName = newName)
            }
        } catch (e: PreferencesException) {
            Log.e(TAG, "updateUser - ${e.message}")
            return Result.Error(error = e.toPreferencesError())
        }

        try {
            val updateUserRequestDto = UpdateUserRequestDto(name = newName)
            userApi.updateUser(updateUserRequestDto = updateUserRequestDto)

            encryptedPreferencesStorage.savePreferences { prefs ->
                prefs.copy(isUserEditSynced = true)
            }
        } catch (e: Exception) {
            Log.e(TAG, "updateUser - ${e.message}")

            try {
                encryptedPreferencesStorage.savePreferences { prefs ->
                    prefs.copy(isUserEditSynced = false)
                }
            } catch (e: PreferencesException) {
                Log.e(TAG, "${e.message}")
            }

            if (e is RemoteBackendException.NetworkError) {
                return Result.Success(data = Unit)
            }

            val error = when (e) {
                is PreferencesException -> e.toPreferencesError()
                is RemoteBackendException -> e.toRemoteBackendError()
                else -> throw e
            }

            return Result.Error(error = error)
        }

        return Result.Success(data = Unit)
    }

    override suspend fun retryUpdateUser() {
        val userFromPreferences = try {
            encryptedPreferencesStorage.readPreferences().first().toUser()
        } catch (e: PreferencesException) {
            Log.e(TAG, "retryUpdateUser - ${e.message}")
            null
        }

        if (userFromPreferences?.name == null) return

        try {
            val updateUserRequestDto = UpdateUserRequestDto(name = userFromPreferences.name)
            userApi.updateUser(updateUserRequestDto = updateUserRequestDto)

            encryptedPreferencesStorage.savePreferences { prefs ->
                prefs.copy(isUserEditSynced = true)
            }
        } catch (e: Exception) {
            Log.e(TAG, "retryUpdateUser - ${e.message}")
        }
    }

    override suspend fun deleteUser(): Result<Unit, DataSourceError> {
        return try {
            userApi.deleteUser()

            encryptedPreferencesStorage.savePreferences { prefs ->
                prefs.copy(
                    sessionCookie = null,
                    displayName = null,
                    profilePictureUrl = null,
                )
            }

            Result.Success(data = Unit)
        } catch (e: Exception) {
            Log.e(TAG, "${e.message}")

            val error = when (e) {
                is PreferencesException -> e.toPreferencesError()
                is RemoteBackendException -> e.toRemoteBackendError()
                else -> throw e
            }
            Result.Error(error = error)
        }
    }

    override suspend fun signOutUser(): Result<Unit, DataSourceError> {
        return try {
            userApi.signOutUser()

            encryptedPreferencesStorage.savePreferences { prefs ->
                prefs.copy(
                    sessionCookie = null,
                    displayName = null,
                    profilePictureUrl = null,
                )
            }

            Result.Success(data = Unit)
        } catch (e: Exception) {
            Log.e(TAG, "${e.message}")

            val error = when (e) {
                is PreferencesException -> e.toPreferencesError()
                is RemoteBackendException -> e.toRemoteBackendError()
                else -> throw e
            }
            Result.Error(error = error)
        }
    }

    companion object {
        private const val TAG = "UserRepositoryImpl"
    }
}
