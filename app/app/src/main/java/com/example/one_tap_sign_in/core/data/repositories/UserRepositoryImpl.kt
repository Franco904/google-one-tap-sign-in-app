package com.example.one_tap_sign_in.core.data.repositories

import com.example.one_tap_sign_in.core.data.local.preferences.UserPreferencesStorage
import com.example.one_tap_sign_in.core.data.models.User
import com.example.one_tap_sign_in.core.data.remote.apis.UserApi
import com.example.one_tap_sign_in.core.data.remote.requestDtos.SignInRequestDto
import com.example.one_tap_sign_in.core.data.remote.requestDtos.UpdateUserRequestDto
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(
    private val userApi: UserApi,
    private val userPreferencesStorage: UserPreferencesStorage,
) : UserRepository {
    override suspend fun isSignedIn(): Boolean {
        return !userPreferencesStorage.readPreferences().first().sessionId.isNullOrBlank()
    }

    override suspend fun signInUser(
        idToken: String,
        displayName: String?,
        profilePictureUrl: String?,
    ) {
        val requestDto = SignInRequestDto(idToken = idToken)
        val responseDto = userApi.signInUser(signInRequestDto = requestDto)

        val sessionId = responseDto.sessionId

        userPreferencesStorage.savePreferences { preferences ->
            preferences.copy(
                sessionId = sessionId,
                displayName = displayName,
                profilePictureUrl = profilePictureUrl,
            )
        }
    }

    override suspend fun getUser() = flow {
        val preferences = userPreferencesStorage.readPreferences().first()

        emit(
            User(
                name = preferences.displayName,
                profilePictureUrl = preferences.profilePictureUrl,
            )
        )

        val responseDto = userApi.getUser()

        emit(
            User(
                email = responseDto.email,
                name = responseDto.name,
                profilePictureUrl = responseDto.profilePictureUrl,
            )
        )
    }

    override suspend fun updateUser(newName: String) {
        val updateUserRequestDto = UpdateUserRequestDto(name = newName)
        userApi.updateUser(updateUserRequestDto = updateUserRequestDto)

        userPreferencesStorage.savePreferences { preferences ->
            preferences.copy(displayName = newName)
        }
    }

    override suspend fun deleteUser() {
        userApi.deleteUser()

        userPreferencesStorage.savePreferences { preferences ->
            preferences.copy(
                sessionId = null,
                displayName = null,
                profilePictureUrl = null,
            )
        }
    }

    override suspend fun signOutUser() {
        userApi.signOutUser()

        userPreferencesStorage.savePreferences { preferences ->
            preferences.copy(
                sessionId = null,
                displayName = null,
                profilePictureUrl = null,
            )
        }
    }
}
