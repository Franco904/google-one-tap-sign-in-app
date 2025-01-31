package com.example.one_tap_sign_in.core.data.repositories

import android.util.Log
import com.example.one_tap_sign_in.core.data.dataSources.http.apis.interfaces.UserApi
import com.example.one_tap_sign_in.core.data.dataSources.http.requestDtos.SignInRequestDto
import com.example.one_tap_sign_in.core.data.dataSources.http.requestDtos.UpdateUserRequestDto
import com.example.one_tap_sign_in.core.data.dataSources.preferences.interfaces.UserPreferencesStorage
import com.example.one_tap_sign_in.core.data.exceptions.HttpException
import com.example.one_tap_sign_in.core.data.exceptions.PreferencesException
import com.example.one_tap_sign_in.core.data.models.User
import com.example.one_tap_sign_in.core.domain.repositories.UserRepository
import com.example.one_tap_sign_in.core.domain.utils.DataSourceError
import com.example.one_tap_sign_in.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(
    private val userApi: UserApi,
    private val userPreferencesStorage: UserPreferencesStorage,
    // private val logger: Logger,
) : UserRepository {
    override suspend fun isUserSignedIn(): Result<Boolean, DataSourceError> {
        return try {
            val isSignedIn = userPreferencesStorage.readPreferences().first().sessionCookie != null
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

    override suspend fun signInUser(
        idToken: String,
        displayName: String?,
        profilePictureUrl: String?,
    ): Result<Unit, DataSourceError> {
        return try {
            val requestDto = SignInRequestDto(idToken = idToken)
            userApi.signInUser(signInRequestDto = requestDto)

            userPreferencesStorage.savePreferences { prefs ->
                prefs.copy(
                    displayName = displayName,
                    profilePictureUrl = profilePictureUrl,
                )
            }

            Result.Success(data = Unit)
        } catch (e: Exception) {
            Log.e(TAG, "${e.message}")

            val error = when (e) {
                is PreferencesException -> e.toPreferencesError()
                is HttpException -> e.toBackendError()
                else -> throw e
            }
            Result.Error(error = error)
        }
    }

    override fun watchUser(): Flow<Result<User, DataSourceError>> = flow {
        try {
            val preferences = userPreferencesStorage.readPreferences().first()

            val userFromPreferences = User(
                name = preferences.displayName,
                profilePictureUrl = preferences.profilePictureUrl,
            )
            emit(Result.Success(data = userFromPreferences))
            val responseDto = userApi.getUser()

            userPreferencesStorage.savePreferences { prefs ->
                prefs.copy(
                    displayName = responseDto.name,
                    profilePictureUrl = responseDto.profilePictureUrl,
                )
            }

            val userFromBackend = User(
                email = responseDto.email,
                name = responseDto.name,
                profilePictureUrl = responseDto.profilePictureUrl,
            )
            emit(Result.Success(data = userFromBackend))

            userPreferencesStorage.readPreferences().collect { prefs ->
                val user = User(
                    name = prefs.displayName,
                    profilePictureUrl = prefs.profilePictureUrl,
                )
                emit(Result.Success(data = user))
            }
        } catch (e: Exception) {
            Log.e(TAG, "${e.message}")

            val error = when (e) {
                is PreferencesException -> e.toPreferencesError()
                is HttpException -> e.toBackendError()
                else -> throw e
            }
            emit(Result.Error(error = error))
        }
    }

    override suspend fun updateUser(newName: String): Result<Unit, DataSourceError> {
        return try {
            val updateUserRequestDto = UpdateUserRequestDto(name = newName)
            userApi.updateUser(
                updateUserRequestDto = updateUserRequestDto
            )

            userPreferencesStorage.savePreferences { prefs ->
                prefs.copy(displayName = newName)
            }

            Result.Success(data = Unit)
        } catch (e: Exception) {
            Log.e(TAG, "${e.message}")

            val error = when (e) {
                is PreferencesException -> e.toPreferencesError()
                is HttpException -> e.toBackendError()
                else -> throw e
            }
            Result.Error(error = error)
        }
    }

    override suspend fun deleteUser(): Result<Unit, DataSourceError> {
        return try {
            userApi.deleteUser()

            userPreferencesStorage.savePreferences { prefs ->
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
                is HttpException -> e.toBackendError()
                else -> throw e
            }
            Result.Error(error = error)
        }
    }

    override suspend fun signOutUser(): Result<Unit, DataSourceError> {
        return try {
            userApi.signOutUser()

            userPreferencesStorage.savePreferences { prefs ->
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
                is HttpException -> e.toBackendError()
                else -> throw e
            }
            Result.Error(error = error)
        }
    }

    companion object {
        private const val TAG = "UserRepositoryImpl"
    }
}
