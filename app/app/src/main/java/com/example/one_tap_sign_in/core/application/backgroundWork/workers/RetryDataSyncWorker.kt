package com.example.one_tap_sign_in.core.application.backgroundWork.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.one_tap_sign_in.core.domain.repositories.RetryDataSyncRepository
import com.example.one_tap_sign_in.core.domain.utils.hasError

class RetryDataSyncWorker(
    applicationContext: Context,
    workerParameters: WorkerParameters,
    private val retryDataSyncRepository: RetryDataSyncRepository,
) : CoroutineWorker(applicationContext, workerParameters) {
    override suspend fun doWork(): Result {
        val result = retryDataSyncRepository.retryDataSync()

        return if (result.hasError()) Result.retry() else Result.success()
    }

    companion object {
        const val UNIQUE_NAME = "RetryDataSyncWorker"
    }
}
