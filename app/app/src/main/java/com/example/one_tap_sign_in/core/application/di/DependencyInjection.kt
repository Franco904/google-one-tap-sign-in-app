package com.example.one_tap_sign_in.core.application.di

import android.app.Application
import com.example.one_tap_sign_in.core.application.di.modules.networkModule
import com.example.one_tap_sign_in.core.application.di.modules.repositoryModule
import com.example.one_tap_sign_in.core.application.di.modules.storageModule
import com.example.one_tap_sign_in.core.application.di.modules.validatorModule
import com.example.one_tap_sign_in.core.application.di.modules.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

fun Application.configureDependencyInjection() {
    startKoin {
        androidLogger()
        androidContext(this@configureDependencyInjection)

        modules(
            viewModelModule,
            validatorModule,
            repositoryModule,
            storageModule,
            networkModule,
        )
    }
}
