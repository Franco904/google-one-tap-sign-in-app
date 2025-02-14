package com.example.one_tap_sign_in.core.data.dataSources.preferences.plain

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.one_tap_sign_in.core.data.dataSources.preferences.interfaces.PreferencesStorage
import com.example.one_tap_sign_in.core.data.exceptions.asPreferencesException
import kotlinx.coroutines.flow.Flow

class PlainPreferencesStorage(
    private val dataStore: DataStore<PlainPreferences>
) : PreferencesStorage<PlainPreferences> {
    override suspend fun savePreferences(onPreferencesFile: (PlainPreferences) -> PlainPreferences) {
        try {
            dataStore.updateData { preferences -> onPreferencesFile(preferences) }
        } catch (e: Exception) {
            throw e.asPreferencesException()
        }
    }

    override suspend fun readPreferences(): Flow<PlainPreferences> {
        return try {
            dataStore.data
        } catch (e: Exception) {
            throw e.asPreferencesException()
        }
    }

    companion object {
        val Context.plainDataStore: DataStore<PlainPreferences> by dataStore(
            fileName = "plain-preferences",
            serializer = PlainPreferencesSerializer,
        )
    }
}
