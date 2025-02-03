package com.example.one_tap_sign_in.core.data.dataSources.preferences.interfaces

import com.example.one_tap_sign_in.core.data.dataSources.preferences.EncryptedPreferences
import kotlinx.coroutines.flow.Flow

interface EncryptedPreferencesStorage {
    suspend fun savePreferences(onPreferencesFile: (EncryptedPreferences) -> EncryptedPreferences)

    suspend fun readPreferences(): Flow<EncryptedPreferences>
}
