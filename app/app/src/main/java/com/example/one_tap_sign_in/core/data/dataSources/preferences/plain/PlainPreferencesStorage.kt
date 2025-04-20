package com.example.one_tap_sign_in.core.data.dataSources.preferences.plain

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.one_tap_sign_in.BuildConfig
import com.example.one_tap_sign_in.core.data.dataSources.preferences.interfaces.PreferencesStorage
import com.example.one_tap_sign_in.core.data.exceptions.toPreferencesException
import com.example.one_tap_sign_in.core.domain.utils.AppResult
import com.example.one_tap_sign_in.core.domain.utils.DataSourceError
import kotlinx.coroutines.flow.Flow

class PlainPreferencesStorage(
    private val dataStore: DataStore<PlainPreferences>
) : PreferencesStorage<PlainPreferences> {
    override suspend fun savePreferences(
        onPreferencesFile: (PlainPreferences) -> PlainPreferences,
    ): AppResult<Unit, DataSourceError.PreferencesError> {
        try {
            dataStore.updateData { preferences -> onPreferencesFile(preferences) }

            return AppResult.Success(data = Unit)
        } catch (e: Exception) {
            val preferencesException = e.toPreferencesException()

            if (BuildConfig.DEBUG) {
                Log.e(TAG, "savePreferences error - ${preferencesException.message}")
                e.printStackTrace()
            }

            return AppResult.Error(error = preferencesException.toPreferencesError())
        }
    }

    override suspend fun readPreferences(): AppResult<Flow<PlainPreferences>, DataSourceError.PreferencesError> {
        try {
            return AppResult.Success(data = dataStore.data)
        } catch (e: Exception) {
            val preferencesException = e.toPreferencesException()

            if (BuildConfig.DEBUG) {
                Log.e(TAG, "readPreferences error - ${preferencesException.message}")
                e.printStackTrace()
            }

            return AppResult.Error(error = preferencesException.toPreferencesError())
        }
    }

    companion object {
        private const val TAG = "PlainPreferencesStorage"

        val Context.plainDataStore: DataStore<PlainPreferences> by dataStore(
            fileName = "plain-preferences",
            serializer = PlainPreferencesSerializer,
        )
    }
}
