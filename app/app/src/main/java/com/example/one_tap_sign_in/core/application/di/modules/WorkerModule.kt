package com.example.one_tap_sign_in.core.application.di.modules

import com.example.one_tap_sign_in.core.application.backgroundWork.workers.RetryDataSyncWorker
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val workerModule = module {
    worker<RetryDataSyncWorker> {
        RetryDataSyncWorker(
            applicationContext = get(),
            workerParameters = get(),
            retryDataSyncRepository = get(),
        )
    }
}
