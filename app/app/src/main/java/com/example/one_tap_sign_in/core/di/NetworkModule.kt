package com.example.one_tap_sign_in.core.di

import com.example.one_tap_sign_in.BuildConfig
import com.example.one_tap_sign_in.core.constants.BASE_URL
import com.example.one_tap_sign_in.core.data.remote.apis.UserApiImpl
import com.example.one_tap_sign_in.core.data.remote.apis.interfaces.UserApi
import com.example.one_tap_sign_in.core.infra.auth.AppCookieStorage
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
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
                header(HttpHeaders.Accept, ContentType.Application.Json)
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

    single<UserApi> {
        UserApiImpl(
            client = get(),
        )
    }
}
