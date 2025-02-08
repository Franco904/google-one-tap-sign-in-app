package com.example.one_tap_sign_in

import android.app.Application
import com.example.one_tap_sign_in.core.application.backgroundWork.configureWorkManager
import com.example.one_tap_sign_in.core.application.di.configureDependencyInjection
import org.koin.core.component.KoinComponent

class OneTapSignInApplication : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()

        configureDependencyInjection()
        configureWorkManager()
    }
}
