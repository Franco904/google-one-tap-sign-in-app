package com.example.one_tap_sign_in

import android.app.Application
import com.example.one_tap_sign_in.core.di.networkModule
import com.example.one_tap_sign_in.core.di.repositoryModule
import com.example.one_tap_sign_in.core.di.storageModule
import com.example.one_tap_sign_in.core.di.utilsModule
import com.example.one_tap_sign_in.core.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class OneTapSignInApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@OneTapSignInApplication)

            modules(
                viewModelModule,
                repositoryModule,
                storageModule,
                networkModule,
                utilsModule,
            )
        }
    }
}
