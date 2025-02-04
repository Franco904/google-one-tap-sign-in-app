package com.example.one_tap_sign_in.core.domain.repositories

interface RetryDataSyncRepository {
    suspend fun retryDataSyncWhenOnline()
}
