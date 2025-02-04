package com.example.one_tap_sign_in

import android.app.Application
import com.example.one_tap_sign_in.core.application.di.configureDependencyInjection
import com.example.one_tap_sign_in.core.application.foregroundServices.AppDataSyncService
import org.koin.android.ext.android.get

class OneTapSignInApplication : Application() {
    private val appDataSyncService by lazy {
        AppDataSyncService(
            retryDataSyncRepository = get(),
        )
    }

    override fun onCreate() {
        super.onCreate()

        configureDependencyInjection()

        appDataSyncService.configureUnsyncedDataSync()
    }
}
