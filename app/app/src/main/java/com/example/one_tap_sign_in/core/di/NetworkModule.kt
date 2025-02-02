package com.example.one_tap_sign_in.core.di

import com.example.one_tap_sign_in.BuildConfig
import com.example.one_tap_sign_in.core.data.constants.BASE_URL
import com.example.one_tap_sign_in.core.data.dataSources.http.HttpClientManager
import com.example.one_tap_sign_in.core.data.dataSources.http.HttpClientManagerImpl
import com.example.one_tap_sign_in.core.data.dataSources.http.apis.UserApiImpl
import com.example.one_tap_sign_in.core.data.dataSources.http.apis.interfaces.UserApi
import com.example.one_tap_sign_in.core.data.dataSources.preferences.AppCookieStorage
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val networkModule = module {
    single<HttpClient> {
        HttpClient(OkHttp) {
            engine {
                config {
                    readTimeout(30, TimeUnit.SECONDS)
                    connectTimeout(30, TimeUnit.SECONDS)
                }
            }

            defaultRequest {
                url(BASE_URL)
                contentType(ContentType.Application.Json)
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }

            install(HttpCookies) {
                storage = AppCookieStorage(
                    userPreferencesStorage = get(),
                )
            }

            install(Logging) {
                logger = Logger.ANDROID
                level = if (BuildConfig.DEBUG) LogLevel.BODY else LogLevel.NONE
            }
        }
    }

    single<HttpClientManager> {
        HttpClientManagerImpl(
            client = get(),
        )
    }

    single<UserApi> {
        UserApiImpl(
            client = get(),
        )
    }
}
