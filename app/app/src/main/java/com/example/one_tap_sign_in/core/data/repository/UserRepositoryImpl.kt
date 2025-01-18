package com.example.one_tap_sign_in.core.data.repository

import com.example.one_tap_sign_in.core.data.local.preferences.UserPreferences
import com.example.one_tap_sign_in.core.data.local.preferences.UserPreferencesStorage
import kotlinx.coroutines.flow.first

class UserRepositoryImpl(
    private val userPreferencesStorage: UserPreferencesStorage,
) : UserRepository {
    override suspend fun writeIdToken(idToken: String) {
        val updatedPreferences = UserPreferences(idToken = idToken)

        userPreferencesStorage.savePreferences(updatedPreferences)
    }

    override suspend fun getIdToken(): String? {
        return userPreferencesStorage.readPreferences().first().idToken
    }
}