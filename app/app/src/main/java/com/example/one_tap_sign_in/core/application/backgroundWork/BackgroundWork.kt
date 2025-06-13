package com.example.one_tap_sign_in.core.application.backgroundWork

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.one_tap_sign_in.core.application.backgroundWork.workers.RetryDataSyncWorker
import java.util.concurrent.TimeUnit

fun Application.configureBackgroundWork() {
    val workManager = WorkManager.getInstance(this)

    workManager.configureRetryDataSyncWorker()
}

private fun WorkManager.configureRetryDataSyncWorker() {
    val workerConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.UNMETERED) // only runs when connected to Wi-Fi
        .build()

    val workRequest = PeriodicWorkRequestBuilder<RetryDataSyncWorker>(
        /* repeatInterval = */ 15, TimeUnit.MINUTES,
    )
        .setConstraints(workerConstraints)
        .build()

    enqueueUniquePeriodicWork(
        RetryDataSyncWorker.UNIQUE_NAME,
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest,
    )
}
