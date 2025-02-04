package com.example.one_tap_sign_in.core.application.foregroundServices

import com.example.one_tap_sign_in.core.domain.repositories.RetryDataSyncRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AppDataSyncService(
    private val retryDataSyncRepository: RetryDataSyncRepository,
) {
    private val scope = CoroutineScope(SupervisorJob())

    fun configureUnsyncedDataSync() {
        scope.launch {
            retryDataSyncRepository.retryDataSyncWhenOnline()
        }
    }
}
