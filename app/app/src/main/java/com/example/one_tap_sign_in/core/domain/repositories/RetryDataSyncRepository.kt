package com.example.one_tap_sign_in.core.domain.repositories

import com.example.one_tap_sign_in.core.domain.utils.DataSourceError
import com.example.one_tap_sign_in.core.domain.utils.Result

interface RetryDataSyncRepository {
    suspend fun retryDataSync(): Result<Unit, DataSourceError>
}
