package com.example.one_tap_sign_in.core.data.repositories

import android.util.Log
import com.example.one_tap_sign_in.core.data.dataSources.preferences.EncryptedPreferences
import com.example.one_tap_sign_in.core.data.dataSources.preferences.interfaces.EncryptedPreferencesStorage
import com.example.one_tap_sign_in.core.data.dataSources.remoteBackend.apis.interfaces.UserApi
import com.example.one_tap_sign_in.core.data.dataSources.remoteBackend.requestDtos.SignInRequestDto
import com.example.one_tap_sign_in.core.data.dataSources.remoteBackend.requestDtos.UpdateUserRequestDto
import com.example.one_tap_sign_in.core.data.exceptions.PreferencesException
import com.example.one_tap_sign_in.core.data.exceptions.RemoteBackendException
import com.example.one_tap_sign_in.core.domain.models.User
import com.example.one_tap_sign_in.core.domain.repositories.UserRepository
import com.example.one_tap_sign_in.core.domain.utils.DataSourceError
import com.example.one_tap_sign_in.core.domain.utils.Result
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class UserRepositoryImpl(
    private val userApi: UserApi,
    private val encryptedPreferencesStorage: EncryptedPreferencesStorage,
    // private val logger: Logger,
) : UserRepository {
    override suspend fun isUserSignedIn(): Result<Boolean, DataSourceError> {
        return try {
            val isSignedIn =
                encryptedPreferencesStorage.readPreferences().first().sessionCookie != null
            Result.Success(data = isSignedIn)
        } catch (e: Exception) {
            Log.e(TAG, "${e.message}")

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
                encryptedPreferencesStorage.readPreferences().first().didExplicitlySignOut != false
            Result.Success(data = didUserExplicitlySignOut)
        } catch (e: Exception) {
            Log.e(TAG, "${e.message}")

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
                    didExplicitlySignOut = false,
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

    override fun watchUser(): Flow<Result<User, DataSourceError>> = flow {
        try {
            // Read cached user data from preferences storage
            val preferences = encryptedPreferencesStorage.readPreferences().first()
            val userFromPreferences = preferences.toUser()

            emit(Result.Success(data = userFromPreferences))

            // Fetch user data from remote backend API
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

            // Watch the cached user data for updates
            emitAll(
                encryptedPreferencesStorage.readPreferences()
                    .map { prefs: EncryptedPreferences ->
                        Result.Success<User, DataSourceError>(data = prefs.toUser())
                    }
                    .distinctUntilChanged()
            )
        } catch (e: Exception) {
            Log.e(TAG, "${e.message}")

            if (e is PreferencesException) {
                emit(Result.Error(error = e.toPreferencesError()))
            }
        }
    }

    override suspend fun updateUser(newName: String): Result<Unit, DataSourceError> {
        return try {
            encryptedPreferencesStorage.savePreferences { prefs ->
                prefs.copy(displayName = newName)
            }

            coroutineScope {
                launch {
                    val updateUserRequestDto = UpdateUserRequestDto(name = newName)
                    userApi.updateUser(updateUserRequestDto = updateUserRequestDto)
                }
            }

            Result.Success(data = Unit)
        } catch (e: Exception) {
            Log.e(TAG, "${e.message}")

            if (e is PreferencesException) {
                Result.Error(error = e.toPreferencesError())
            } else {
                Result.Success(data = Unit)
            }
        }
    }

    override suspend fun deleteUser(): Result<Unit, DataSourceError> {
        return try {
            encryptedPreferencesStorage.savePreferences { prefs ->
                prefs.copy(
                    sessionCookie = null,
                    displayName = null,
                    profilePictureUrl = null,
                    didExplicitlySignOut = true,
                )
            }

            coroutineScope {
                launch {
                    userApi.deleteUser()
                }
            }

            Result.Success(data = Unit)
        } catch (e: Exception) {
            Log.e(TAG, "${e.message}")

            if (e is PreferencesException) {
                Result.Error(error = e.toPreferencesError())
            } else {
                Result.Success(data = Unit)
            }
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
                    didExplicitlySignOut = true,
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
