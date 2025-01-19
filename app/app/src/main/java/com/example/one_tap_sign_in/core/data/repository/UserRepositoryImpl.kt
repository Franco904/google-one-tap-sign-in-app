package com.example.one_tap_sign_in.core.data.repository

import com.example.one_tap_sign_in.core.data.local.preferences.UserPreferences
import com.example.one_tap_sign_in.core.data.local.preferences.UserPreferencesStorage
import kotlinx.coroutines.flow.first

class UserRepositoryImpl(
    private val userPreferencesStorage: UserPreferencesStorage,
) : UserRepository {
    override suspend fun authenticateUser(idToken: String) {
        // TODO: Implement back-end call.
    }

    override suspend fun writeIsSignedIn(isSignedIn: Boolean) {
        val updatedPreferences = UserPreferences(isSignedIn = isSignedIn)
        userPreferencesStorage.savePreferences(updatedPreferences)
    }

    override suspend fun isSignedIn(): Boolean? {
        return userPreferencesStorage.readPreferences().first().isSignedIn
    }
}
