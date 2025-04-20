package com.example.one_tap_sign_in.androidTestUtils

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters

fun workerFactory(
    workerBuilder: (Context, WorkerParameters) -> ListenableWorker,
): WorkerFactory {
    return object : WorkerFactory() {
        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
        ) = workerBuilder(appContext, workerParameters)
    }
}
