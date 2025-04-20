package com.example.one_tap_sign_in.core.application.backgroundWork.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.one_tap_sign_in.core.domain.repositories.UserRepository
import com.example.one_tap_sign_in.core.domain.utils.hasError

class RetryDataSyncWorker(
    applicationContext: Context,
    workerParameters: WorkerParameters,
    private val userRepository: UserRepository,
) : CoroutineWorker(applicationContext, workerParameters) {
    override suspend fun doWork(): Result {
        val result = userRepository.retrySendUserUpdate()

        return if (result.hasError()) Result.failure() else Result.success()
    }

    companion object {
        const val UNIQUE_NAME = "RetryDataSyncWorker"
    }
}
