package com.example.one_tap_sign_in.core.data.repositories

import android.util.Log
import com.example.one_tap_sign_in.core.data.dataSources.preferences.encrypted.EncryptedPreferences
import com.example.one_tap_sign_in.core.data.dataSources.preferences.interfaces.PreferencesStorage
import com.example.one_tap_sign_in.core.data.exceptions.PreferencesException
import com.example.one_tap_sign_in.core.domain.repositories.RetryDataSyncRepository
import com.example.one_tap_sign_in.core.domain.repositories.UserRepository
import com.example.one_tap_sign_in.core.domain.utils.DataSourceError
import com.example.one_tap_sign_in.core.domain.utils.Result
import kotlinx.coroutines.flow.first

class RetryDataSyncRepositoryImpl(
    private val encryptedPreferencesStorage: PreferencesStorage<EncryptedPreferences>,
    private val userRepository: UserRepository,
) : RetryDataSyncRepository {
    override suspend fun retryDataSync(): Result<Unit, DataSourceError> {
        return try {
            val encryptedPreferences = encryptedPreferencesStorage.readPreferences().first()

            val isUserSignedIn = encryptedPreferences.sessionCookie != null
            if (isUserSignedIn) {
                val isUserEditSynced = encryptedPreferences.isUserEditSynced == false

                if (isUserEditSynced) userRepository.retryUpdateUser()
            }

            Result.Success(data = Unit)
        } catch (e: Exception) {
            Log.e(TAG, "retryDataSyncWhenOnline - ${e.message}")

            val error = when (e) {
                is PreferencesException -> e.toPreferencesError()
                else -> throw e
            }

            Result.Error(error = error)
        }
    }

    companion object {
        private const val TAG = "DataSyncRepositoryImpl"
    }
}
