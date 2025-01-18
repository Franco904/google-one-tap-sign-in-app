package com.example.one_tap_sign_in.core.data.local.preferences

import kotlinx.coroutines.flow.Flow

interface UserPreferencesStorage {
    suspend fun savePreferences(preferences: UserPreferences)

    suspend fun readPreferences(): Flow<UserPreferences>
}
