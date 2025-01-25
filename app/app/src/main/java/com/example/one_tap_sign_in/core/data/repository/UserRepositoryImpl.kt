package com.example.one_tap_sign_in.core.data.repository

import com.example.one_tap_sign_in.core.data.local.preferences.UserPreferencesStorage
import com.example.one_tap_sign_in.core.data.remote.apis.UserApi
import com.example.one_tap_sign_in.core.data.remote.requestDtos.SignInRequestDto
import kotlinx.coroutines.flow.first

class UserRepositoryImpl(
    private val userApi: UserApi,
    private val userPreferencesStorage: UserPreferencesStorage,
) : UserRepository {
    override suspend fun authenticateUser(idToken: String) {
        val requestDto = SignInRequestDto(idToken = idToken)
        val responseDto = userApi.signInUser(signInRequestDto = requestDto)

        val sessionId = responseDto.sessionId
    }

    override suspend fun saveUserCredentials(
        displayName: String?,
        profilePictureUrl: String?,
    ) {
        userPreferencesStorage.savePreferences { preferences ->
            preferences.copy(
                displayName = displayName,
                profilePictureUrl = profilePictureUrl,
            )
        }
    }

    override suspend fun readUserCredentials(): Pair<String?, String?> {
        val preferences = userPreferencesStorage.readPreferences().first()

        return Pair(
            preferences.displayName,
            preferences.profilePictureUrl,
        )
    }

    override suspend fun deleteUserCredentials() {
        userPreferencesStorage.savePreferences { preferences ->
            preferences.copy(
                displayName = null,
                profilePictureUrl = null,
            )
        }
    }

    override suspend fun saveIsSignedIn(isSignedIn: Boolean) {
        userPreferencesStorage.savePreferences { preferences ->
            preferences.copy(
                isSignedIn = isSignedIn,
            )
        }
    }

    override suspend fun readIsSignedIn(): Boolean? {
        return userPreferencesStorage.readPreferences().first().isSignedIn
    }
}
