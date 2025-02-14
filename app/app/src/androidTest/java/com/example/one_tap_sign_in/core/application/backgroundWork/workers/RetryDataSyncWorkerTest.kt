package com.example.one_tap_sign_in.core.application.backgroundWork.workers

import android.content.Context
import androidTestUtils.workerFactory
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.example.one_tap_sign_in.core.domain.repositories.RetryDataSyncRepository
import com.example.one_tap_sign_in.core.domain.utils.DataSourceError
import com.example.one_tap_sign_in.core.domain.utils.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test

class RetryDataSyncWorkerTest {
    private lateinit var sut: RetryDataSyncWorker

    private lateinit var retryDataSyncRepositoryMock: RetryDataSyncRepository

    private lateinit var appContext: Context

    @Before
    fun setUp() {
        retryDataSyncRepositoryMock = mockk(relaxUnitFun = true)

        appContext = InstrumentationRegistry.getInstrumentation().context

        sut = TestListenableWorkerBuilder<RetryDataSyncWorker>(appContext)
            .setWorkerFactory(workerFactory { context, params ->
                RetryDataSyncWorker(
                    applicationContext = context,
                    workerParameters = params,
                    retryDataSyncRepository = retryDataSyncRepositoryMock,
                )
            })
            .build()
    }

    @Test
    fun shouldReturnFailureResultIfTheRetryReturnsAnError(): Unit = runBlocking {
        coEvery { retryDataSyncRepositoryMock.retryDataSync() } returns
                Result.Error(error = DataSourceError.RemoteBackendError.UnknownError)

        val result = sut.doWork()

        result.shouldBeEqualTo(ListenableWorker.Result.failure())
    }

    @Test
    fun shouldReturnSuccessResultIfTheRetryDoesNotReturnAnError(): Unit = runBlocking {
        coEvery { retryDataSyncRepositoryMock.retryDataSync() } returns
                Result.Success(data = Unit)

        val result = sut.doWork()

        result.shouldBeEqualTo(ListenableWorker.Result.success())
    }
}
