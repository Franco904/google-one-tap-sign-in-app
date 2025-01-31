package com.example.one_tap_sign_in.core.data.dataSources.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.one_tap_sign_in.core.data.dataSources.preferences.interfaces.UserPreferencesStorage
import com.example.one_tap_sign_in.core.data.exceptions.toPreferencesException
import kotlinx.coroutines.flow.Flow

class UserPreferencesStorageImpl(
    private val dataStore: DataStore<UserPreferences>
) : UserPreferencesStorage {
    override suspend fun savePreferences(onPreferencesFile: (UserPreferences) -> UserPreferences) {
        try {
            dataStore.updateData { preferences -> onPreferencesFile(preferences) }
        } catch (e: Exception) {
            throw e.toPreferencesException()
        }
    }

    override suspend fun readPreferences(): Flow<UserPreferences> {
        return try {
            dataStore.data
        } catch (e: Exception) {
            throw e.toPreferencesException()
        }
    }

    companion object {
        val Context.dataStore: DataStore<UserPreferences> by dataStore(
            fileName = "user-preferences",
            serializer = UserPreferencesSerializer,
        )
    }
}
