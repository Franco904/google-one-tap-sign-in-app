package com.example.one_tap_sign_in.core.data.dataSources.preferences.interfaces

import kotlinx.coroutines.flow.Flow

interface PreferencesStorage<T> {
    suspend fun savePreferences(onPreferencesFile: (T) -> T)

    suspend fun readPreferences(): Flow<T>
}
