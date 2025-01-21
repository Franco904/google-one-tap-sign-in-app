package com.example.one_tap_sign_in.core.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.coroutines.flow.Flow

class DataStoreUserPreferencesStorage(
    private val dataStore: DataStore<UserPreferences>
) : UserPreferencesStorage {
    override suspend fun savePreferences(onPreferencesFile: (UserPreferences) -> UserPreferences) {
        dataStore.updateData { preferences -> onPreferencesFile(preferences) }
    }

    override suspend fun readPreferences(): Flow<UserPreferences> {
        return dataStore.data
    }

    companion object {
        val Context.dataStore: DataStore<UserPreferences> by dataStore(
            fileName = "user-preferences",
            serializer = UserPreferencesSerializer,
        )
    }
}
