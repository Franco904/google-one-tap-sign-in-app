package com.example.one_tap_sign_in.core.data.dataSources.preferences.encrypted

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.one_tap_sign_in.core.data.dataSources.preferences.interfaces.PreferencesStorage
import com.example.one_tap_sign_in.core.data.exceptions.asPreferencesException
import kotlinx.coroutines.flow.Flow

class EncryptedPreferencesStorage(
    private val dataStore: DataStore<EncryptedPreferences>
) : PreferencesStorage<EncryptedPreferences> {
    override suspend fun savePreferences(onPreferencesFile: (EncryptedPreferences) -> EncryptedPreferences) {
        try {
            dataStore.updateData { preferences -> onPreferencesFile(preferences) }
        } catch (e: Exception) {
            throw e.asPreferencesException()
        }
    }

    override suspend fun readPreferences(): Flow<EncryptedPreferences> {
        return try {
            dataStore.data
        } catch (e: Exception) {
            throw e.asPreferencesException()
        }
    }

    companion object {
        val Context.encryptedDataStore: DataStore<EncryptedPreferences> by dataStore(
            fileName = "encrypted-preferences",
            serializer = EncryptedPreferencesSerializer,
        )
    }
}
