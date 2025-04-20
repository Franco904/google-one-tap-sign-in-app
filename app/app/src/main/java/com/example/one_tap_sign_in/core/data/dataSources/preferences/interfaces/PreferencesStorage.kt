package com.example.one_tap_sign_in.core.data.dataSources.preferences.interfaces

import com.example.one_tap_sign_in.core.domain.utils.AppResult
import com.example.one_tap_sign_in.core.domain.utils.DataSourceError
import kotlinx.coroutines.flow.Flow

interface PreferencesStorage<T> {
    suspend fun savePreferences(
        onPreferencesFile: (T) -> T,
    ): AppResult<Unit, DataSourceError.PreferencesError>

    suspend fun readPreferences(): AppResult<Flow<T>, DataSourceError.PreferencesError>
}
