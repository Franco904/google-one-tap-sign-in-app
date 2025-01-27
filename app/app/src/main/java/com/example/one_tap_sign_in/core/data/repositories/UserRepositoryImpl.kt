package com.example.one_tap_sign_in.core.data.repositories

import com.example.one_tap_sign_in.core.data.local.preferences.UserPreferencesStorage
import com.example.one_tap_sign_in.core.data.models.User
import com.example.one_tap_sign_in.core.data.remote.apis.interfaces.UserApi
import com.example.one_tap_sign_in.core.data.remote.requestDtos.SignInRequestDto
import com.example.one_tap_sign_in.core.data.remote.requestDtos.UpdateUserRequestDto
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(
    private val userApi: UserApi,
    private val userPreferencesStorage: UserPreferencesStorage,
) : UserRepository {
    override suspend fun isSignedIn(): Boolean {
        return userPreferencesStorage.readPreferences().first().sessionCookie != null
    }

    override suspend fun signInUser(
        idToken: String,
        displayName: String?,
        profilePictureUrl: String?,
    ) {
        val requestDto = SignInRequestDto(idToken = idToken)
        userApi.signInUser(signInRequestDto = requestDto)

        userPreferencesStorage.savePreferences { prefs ->
            prefs.copy(
                displayName = displayName,
                profilePictureUrl = profilePictureUrl,
            )
        }
    }

    override fun watchUser() = flow {
        val preferences = userPreferencesStorage.readPreferences().first()

        emit(
            User(
                name = preferences.displayName,
                profilePictureUrl = preferences.profilePictureUrl,
            )
        )

        try {
            val responseDto = userApi.getUser()

            userPreferencesStorage.savePreferences { prefs ->
                prefs.copy(
                    displayName = responseDto.name,
                    profilePictureUrl = responseDto.profilePictureUrl,
                )
            }

            emit(
                User(
                    email = responseDto.email,
                    name = responseDto.name,
                    profilePictureUrl = responseDto.profilePictureUrl,
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        userPreferencesStorage.readPreferences().collect { prefs ->
            emit(
                User(
                    name = prefs.displayName,
                    profilePictureUrl = prefs.profilePictureUrl,
                )
            )
        }
    }

    override suspend fun updateUser(newName: String) {
        val updateUserRequestDto = UpdateUserRequestDto(name = newName)
        userApi.updateUser(
            updateUserRequestDto = updateUserRequestDto
        )

        userPreferencesStorage.savePreferences { prefs ->
            prefs.copy(displayName = newName)
        }
    }

    override suspend fun deleteUser() {
        userApi.deleteUser()

        userPreferencesStorage.savePreferences { prefs ->
            prefs.copy(
                sessionCookie = null,
                displayName = null,
                profilePictureUrl = null,
            )
        }
    }

    override suspend fun signOutUser() {
        userApi.signOutUser()

        userPreferencesStorage.savePreferences { prefs ->
            prefs.copy(
                sessionCookie = null,
                displayName = null,
                profilePictureUrl = null,
            )
        }
    }
}
