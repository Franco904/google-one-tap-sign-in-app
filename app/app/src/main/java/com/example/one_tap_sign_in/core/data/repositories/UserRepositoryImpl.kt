package com.example.one_tap_sign_in.core.data.repositories

import com.example.one_tap_sign_in.core.data.local.preferences.UserPreferencesStorage
import com.example.one_tap_sign_in.core.data.remote.apis.UserApi
import com.example.one_tap_sign_in.core.data.remote.requestDtos.SignInRequestDto
import kotlinx.coroutines.flow.first

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

    override suspend fun readUserCredentials(): Pair<String?, String?> {
        val preferences = userPreferencesStorage.readPreferences().first()

        return Pair(
            preferences.displayName,
            preferences.profilePictureUrl,
        )
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
