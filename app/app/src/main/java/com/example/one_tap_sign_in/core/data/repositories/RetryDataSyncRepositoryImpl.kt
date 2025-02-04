package com.example.one_tap_sign_in.core.data.repositories

import android.util.Log
import com.example.one_tap_sign_in.core.data.dataSources.connectivity.interfaces.ConnectivityObserver
import com.example.one_tap_sign_in.core.data.dataSources.preferences.encrypted.EncryptedPreferences
import com.example.one_tap_sign_in.core.data.dataSources.preferences.interfaces.PreferencesStorage
import com.example.one_tap_sign_in.core.domain.repositories.RetryDataSyncRepository
import com.example.one_tap_sign_in.core.domain.repositories.UserRepository
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first

class RetryDataSyncRepositoryImpl(
    private val connectivityObserver: ConnectivityObserver,
    private val encryptedPreferencesStorage: PreferencesStorage<EncryptedPreferences>,
    private val userRepository: UserRepository,
) : RetryDataSyncRepository {
    override suspend fun retryDataSyncWhenOnline() {
        try {
            connectivityObserver.isConnectedToInternet()
                .distinctUntilChanged()
                .filter { it }
                .collect {
                    val isUserEditSynced =
                        encryptedPreferencesStorage.readPreferences()
                            .first().isUserEditSynced == false

                    if (isUserEditSynced) userRepository.retryUpdateUser()
                }
        } catch (e: Exception) {
            Log.e(TAG, "${e.message}")
        }
    }

    companion object {
        private const val TAG = "DataSyncRepositoryImpl"
    }
}
