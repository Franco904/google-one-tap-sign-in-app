package com.example.one_tap_sign_in.core.di

import com.example.one_tap_sign_in.core.data.utils.CryptoUtils
import org.koin.dsl.module

val utilsModule = module {
    single { CryptoUtils() }
}
