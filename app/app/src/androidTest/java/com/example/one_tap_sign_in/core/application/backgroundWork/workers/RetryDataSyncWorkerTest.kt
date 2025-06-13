package com.example.one_tap_sign_in.core.application.backgroundWork.workers

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.testing.WorkManagerTestInitHelper
import com.example.one_tap_sign_in.androidTestUtils.workerFactory
import com.example.one_tap_sign_in.core.domain.repositories.UserRepository
import com.example.one_tap_sign_in.core.domain.utils.AppResult
import com.example.one_tap_sign_in.core.domain.utils.DataSourceError
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test

class RetryDataSyncWorkerTest {
    private lateinit var userRepositoryMock: UserRepository

    private lateinit var appContext: Context

    private lateinit var workManager: WorkManager
    private lateinit var currentWorkRequest: WorkRequest

    @Before
    fun setUp() {
        userRepositoryMock = mockk(relaxUnitFun = true)

        appContext = ApplicationProvider.getApplicationContext()

        val factory = workerFactory { context, params ->
            RetryDataSyncWorker(
                applicationContext = context,
                workerParameters = params,
                userRepository = userRepositoryMock,
            )
        }

        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(factory)
            .build()

        WorkManagerTestInitHelper.initializeTestWorkManager(appContext, config)
        workManager = WorkManager.getInstance(appContext)
    }

    @Test
    fun shouldReturnFailureResultIfTheRetryReturnsAnError(): Unit = runBlocking {
        coEvery { userRepositoryMock.retrySendUserUpdate() } returns
                AppResult.Error(error = DataSourceError.RemoteBackendError.UnknownError)

        enqueueRetryDataSyncWork()

        val result = awaitWorkToFinish()

        result.state shouldBe WorkInfo.State.FAILED
    }

    @Test
    fun shouldReturnSuccessResultIfTheRetryDoesNotReturnAnError(): Unit = runBlocking {
        coEvery { userRepositoryMock.retrySendUserUpdate() } returns
                AppResult.Success(data = Unit)

        enqueueRetryDataSyncWork()

        val result = awaitWorkToFinish()

        result.state shouldBe WorkInfo.State.SUCCEEDED
    }

    private fun enqueueRetryDataSyncWork() {
        val workerConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED) // only runs when connected to Wi-Fi
            .build()

        val workRequest = OneTimeWorkRequestBuilder<RetryDataSyncWorker>()
            .setConstraints(workerConstraints)
            .build()

        workManager.enqueueUniqueWork(
            RetryDataSyncWorker.UNIQUE_NAME,
            ExistingWorkPolicy.KEEP,
            workRequest,
        ).result.get()

        // Advance in time to execute enqueued workers
        WorkManagerTestInitHelper.getTestDriver(appContext)!!.apply {
            setAllConstraintsMet(workRequest.id)
            setInitialDelayMet(workRequest.id)
        }

        currentWorkRequest = workRequest
    }

    private fun awaitWorkToFinish(): WorkInfo {
        while (true) {
            val info = workManager.getWorkInfoById(currentWorkRequest.id).get()

            if (info?.state?.isFinished == true) return info

            Thread.sleep(100) // Avoids aggressive busy waiting
        }
    }
}
