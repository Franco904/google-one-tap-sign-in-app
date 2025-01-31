package com.example.one_tap_sign_in.core.data.dataSources.preferences.interfaces

import com.example.one_tap_sign_in.core.data.dataSources.preferences.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesStorage {
    suspend fun savePreferences(onPreferencesFile: (UserPreferences) -> UserPreferences)

    suspend fun readPreferences(): Flow<UserPreferences>
}
