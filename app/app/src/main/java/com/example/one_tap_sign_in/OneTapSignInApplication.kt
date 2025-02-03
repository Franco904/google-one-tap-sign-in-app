package com.example.one_tap_sign_in

import android.app.Application
import com.example.one_tap_sign_in.core.application.di.configureDependencyInjection

class OneTapSignInApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        configureDependencyInjection()
    }
}
