package com.example.one_tap_sign_in.core.data.dataSources.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.one_tap_sign_in.core.data.dataSources.preferences.interfaces.EncryptedPreferencesStorage
import com.example.one_tap_sign_in.core.data.exceptions.toPreferencesException
import kotlinx.coroutines.flow.Flow

class EncryptedPreferencesStorageImpl(
    private val dataStore: DataStore<EncryptedPreferences>
) : EncryptedPreferencesStorage {
    override suspend fun savePreferences(onPreferencesFile: (EncryptedPreferences) -> EncryptedPreferences) {
        try {
            dataStore.updateData { preferences -> onPreferencesFile(preferences) }
        } catch (e: Exception) {
            throw e.toPreferencesException()
        }
    }

    override suspend fun readPreferences(): Flow<EncryptedPreferences> {
        return try {
            dataStore.data
        } catch (e: Exception) {
            throw e.toPreferencesException()
        }
    }

    companion object {
        val Context.encryptedDataStore: DataStore<EncryptedPreferences> by dataStore(
            fileName = "encrypted-preferences",
            serializer = EncryptedPreferencesSerializer,
        )
    }
}
