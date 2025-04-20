package com.example.one_tap_sign_in.core.application.backgroundWork.workers

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.example.one_tap_sign_in.androidTestUtils.workerFactory
import com.example.one_tap_sign_in.core.domain.repositories.UserRepository
import com.example.one_tap_sign_in.core.domain.utils.AppResult
import com.example.one_tap_sign_in.core.domain.utils.DataSourceError
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test

class RetryDataSyncWorkerTest {
    private lateinit var sut: RetryDataSyncWorker

    private lateinit var userRepositoryMock: UserRepository

    private lateinit var appContext: Context

    @Before
    fun setUp() {
        userRepositoryMock = mockk(relaxUnitFun = true)

        appContext = InstrumentationRegistry.getInstrumentation().context

        val factory = workerFactory { context, params ->
            RetryDataSyncWorker(
                applicationContext = context,
                workerParameters = params,
                userRepository = userRepositoryMock,
            )
        }

        sut = TestListenableWorkerBuilder<RetryDataSyncWorker>(appContext)
            .setWorkerFactory(factory)
            .build()
    }

    @Test
    fun shouldReturnFailureResultIfTheRetryReturnsAnError(): Unit = runBlocking {
        coEvery { userRepositoryMock.retrySendUserUpdate() } returns
                AppResult.Error(error = DataSourceError.RemoteBackendError.UnknownError)

        val result = sut.doWork()

        result.shouldBeEqualTo(ListenableWorker.Result.failure())
    }

    @Test
    fun shouldReturnSuccessResultIfTheRetryDoesNotReturnAnError(): Unit = runBlocking {
        coEvery { userRepositoryMock.retrySendUserUpdate() } returns
                AppResult.Success(data = Unit)

        val result = sut.doWork()

        result.shouldBeEqualTo(ListenableWorker.Result.success())
    }
}
