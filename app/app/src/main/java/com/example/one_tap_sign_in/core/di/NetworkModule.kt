package com.example.one_tap_sign_in.core.di

import com.example.one_tap_sign_in.core.constants.BASE_URL
import com.example.one_tap_sign_in.core.data.local.preferences.UserPreferencesStorage
import com.example.one_tap_sign_in.core.data.remote.apis.UserApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieHandler
import java.net.CookieManager
import java.util.concurrent.TimeUnit

val networkModule = module {
    single<CookieHandler> { CookieManager() }

    single {
        OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .cookieJar(JavaNetCookieJar(get()))
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        val retrofit = get<Retrofit>()
        retrofit.create(UserApi::class.java)
    }
}
