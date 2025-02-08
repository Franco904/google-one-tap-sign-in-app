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
import com.example.one_tap_sign_in.core.domain.utils.onError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

typealias WatchUserFlowCollector = FlowCollector<Result<User, DataSourceError>>

class UserRepositoryImpl(
    private val userApi: UserApi,
    private val encryptedPreferencesStorage: PreferencesStorage<EncryptedPreferences>,
    private val plainPreferencesStorage: PreferencesStorage<PlainPreferences>,
) : UserRepository {
    override val redirectErrors = listOf(
        DataSourceError.RemoteBackendError.Unauthorized,
        DataSourceError.RemoteBackendError.NotFound,
    )

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
                    isUserEditSynced = true,
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
        emitCachedUser()
        fetchAndCacheRemoteUser()
        watchCachedUserForUpdates()
    }

    private suspend fun WatchUserFlowCollector.emitCachedUser() {
        try {
            val preferences = encryptedPreferencesStorage.readPreferences().first()
            val cachedUser = preferences.toUser()

            if (preferences.sessionCookie == null) {
                emit(Result.Error(error = DataSourceError.RemoteBackendError.Unauthorized))
            }

            emit(Result.Success(data = cachedUser))
        } catch (e: PreferencesException) {
            Log.e(TAG, "watchUser - ${e.message}")
            emit(Result.Error(error = e.toPreferencesError()))
        }
    }

    private suspend fun WatchUserFlowCollector.fetchAndCacheRemoteUser() {
        try {
            val isUserEditSynced =
                encryptedPreferencesStorage.readPreferences().first().isUserEditSynced
            if (!isUserEditSynced) return

            // Fetch user from remote backend API
            val responseDto = userApi.getUser()
            val fetchedUser = responseDto.toUser()

            // Cache the fetched user
            encryptedPreferencesStorage.savePreferences { prefs ->
                prefs.copy(
                    displayName = fetchedUser.name,
                    profilePictureUrl = fetchedUser.profilePictureUrl,
                )
            }

            emit(Result.Success(data = fetchedUser))
        } catch (e: Exception) {
            Log.e(TAG, "watchUser - ${e.message}")

            if (e is PreferencesException) {
                emit(Result.Error(error = e.toPreferencesError()))
            }
        }
    }

    private suspend fun WatchUserFlowCollector.watchCachedUserForUpdates() {
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
        cacheUserUpdate(newName = newName).onError { return Result.Error(error = it) }

        trySendUserUpdate(newName = newName).onError {
            if (it in redirectErrors) {
                deleteCachedUser().onError { e -> return Result.Error(error = e) }
            }

            return Result.Error(error = it)
        }

        return Result.Success(data = Unit)
    }

    private suspend fun cacheUserUpdate(newName: String): Result<Unit, DataSourceError> {
        return try {
            encryptedPreferencesStorage.savePreferences { prefs ->
                prefs.copy(displayName = newName)
            }

            Result.Success(data = Unit)
        } catch (e: PreferencesException) {
            Log.e(TAG, "updateUser - ${e.message}")
            Result.Error(error = e.toPreferencesError())
        }
    }

    private suspend fun trySendUserUpdate(newName: String): Result<Unit, DataSourceError> {
        return try {
            val updateUserRequestDto = UpdateUserRequestDto(name = newName)
            userApi.updateUser(updateUserRequestDto = updateUserRequestDto)

            encryptedPreferencesStorage.savePreferences { prefs ->
                prefs.copy(isUserEditSynced = true)
            }

            Result.Success(data = Unit)
        } catch (e: Exception) {
            Log.e(TAG, "updateUser - ${e.message}")

            onUpdateUserFail().onError { return Result.Error(error = it) }

            val error = when (e) {
                is PreferencesException -> e.toPreferencesError()
                is RemoteBackendException -> e.toRemoteBackendError()
                else -> throw e
            }

            Result.Error(error = error)
        }
    }

    private suspend fun onUpdateUserFail(): Result<Unit, DataSourceError> {
        return try {
            encryptedPreferencesStorage.savePreferences { prefs ->
                prefs.copy(isUserEditSynced = false)
            }

            Result.Success(data = Unit)
        } catch (e: PreferencesException) {
            Log.e(TAG, "updateUser - ${e.message}")

            Result.Error(error = e.toPreferencesError())
        }
    }

    override suspend fun retryUpdateUser() {
        val cachedUser = try {
            encryptedPreferencesStorage.readPreferences().first().toUser()
        } catch (e: PreferencesException) {
            Log.e(TAG, "retryUpdateUser - ${e.message}")
            null
        }

        if (cachedUser?.name == null) return

        try {
            val updateUserRequestDto = UpdateUserRequestDto(name = cachedUser.name)
            userApi.updateUser(updateUserRequestDto = updateUserRequestDto)

            encryptedPreferencesStorage.savePreferences { prefs ->
                prefs.copy(isUserEditSynced = true)
            }
        } catch (e: Exception) {
            Log.e(TAG, "retryUpdateUser - ${e.message}")

            (e as? RemoteBackendException)?.toRemoteBackendError()?.let { backendError ->
                if (backendError in redirectErrors) deleteCachedUser()
            }
        }
    }

    override suspend fun deleteUser(): Result<Unit, DataSourceError> {
        return try {
            userApi.deleteUser()

            deleteCachedUser().onError { return Result.Error(error = it) }

            Result.Success(data = Unit)
        } catch (e: Exception) {
            Log.e(TAG, "${e.message}")

            val error = when (e) {
                is PreferencesException -> e.toPreferencesError()
                is RemoteBackendException -> {
                    val backendError = e.toRemoteBackendError()
                    if (backendError in redirectErrors) {
                        deleteCachedUser().onError { e -> return Result.Error(error = e) }
                    }

                    backendError
                }

                else -> throw e
            }
            Result.Error(error = error)
        }
    }

    override suspend fun signOutUser(): Result<Unit, DataSourceError> {
        return try {
            userApi.signOutUser()

            deleteCachedUser().onError { return Result.Error(error = it) }

            Result.Success(data = Unit)
        } catch (e: Exception) {
            Log.e(TAG, "${e.message}")

            val error = when (e) {
                is PreferencesException -> e.toPreferencesError()
                is RemoteBackendException -> {
                    val backendError = e.toRemoteBackendError()
                    if (backendError in redirectErrors) {
                        deleteCachedUser().onError { e -> return Result.Error(error = e) }
                    }

                    backendError
                }

                else -> throw e
            }
            Result.Error(error = error)
        }
    }

    private suspend fun deleteCachedUser(): Result<Unit, DataSourceError> {
        try {
            encryptedPreferencesStorage.savePreferences { prefs ->
                prefs.copy(
                    sessionCookie = null,
                    displayName = null,
                    profilePictureUrl = null,
                )
            }

            return Result.Success(data = Unit)
        } catch (e: Exception) {
            Log.e(TAG, "${e.message}")

            val error = when (e) {
                is PreferencesException -> e.toPreferencesError()
                else -> throw e
            }

            return Result.Error(error = error)
        }
    }

    companion object {
        private const val TAG = "UserRepositoryImpl"
    }
}
