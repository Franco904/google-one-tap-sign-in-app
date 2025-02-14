package com.example.one_tap_sign_in.core.application.di.modules

import android.content.Context
import androidx.datastore.core.DataStore
import com.example.one_tap_sign_in.BuildConfig
import com.example.one_tap_sign_in.core.application.di.qualifiers.encryptedPreferencesQualifier
import com.example.one_tap_sign_in.core.application.di.qualifiers.plainPreferencesQualifier
import com.example.one_tap_sign_in.core.data.constants.BASE_URL
import com.example.one_tap_sign_in.core.data.dataSources.cookieStorage.AppCookieStorage
import com.example.one_tap_sign_in.core.data.dataSources.preferences.encrypted.EncryptedPreferences
import com.example.one_tap_sign_in.core.data.dataSources.preferences.encrypted.EncryptedPreferencesStorage
import com.example.one_tap_sign_in.core.data.dataSources.preferences.encrypted.EncryptedPreferencesStorage.Companion.encryptedDataStore
import com.example.one_tap_sign_in.core.data.dataSources.preferences.interfaces.PreferencesStorage
import com.example.one_tap_sign_in.core.data.dataSources.preferences.plain.PlainPreferences
import com.example.one_tap_sign_in.core.data.dataSources.preferences.plain.PlainPreferencesStorage
import com.example.one_tap_sign_in.core.data.dataSources.preferences.plain.PlainPreferencesStorage.Companion.plainDataStore
import com.example.one_tap_sign_in.core.data.dataSources.remoteBackend.HttpClientManager
import com.example.one_tap_sign_in.core.data.dataSources.remoteBackend.HttpClientManagerImpl
import com.example.one_tap_sign_in.core.data.dataSources.remoteBackend.apis.UserApiImpl
import com.example.one_tap_sign_in.core.data.dataSources.remoteBackend.apis.interfaces.UserApi
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
import org.koin.core.module.Module
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val dataSourceModule = module {
    configurePreferencesDependencies()
    configureRemoteBackendDependencies()
}

fun Module.configurePreferencesDependencies() {
    single<DataStore<EncryptedPreferences>>(encryptedPreferencesQualifier) {
        get<Context>().encryptedDataStore
    }

    single<DataStore<PlainPreferences>>(plainPreferencesQualifier) {
        get<Context>().plainDataStore
    }

    single<PreferencesStorage<EncryptedPreferences>>(encryptedPreferencesQualifier) {
        EncryptedPreferencesStorage(dataStore = get(encryptedPreferencesQualifier))
    }

    single<PreferencesStorage<PlainPreferences>>(plainPreferencesQualifier) {
        PlainPreferencesStorage(dataStore = get(plainPreferencesQualifier))
    }
}

fun Module.configureRemoteBackendDependencies() {
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
                    encryptedPreferencesStorage = get(encryptedPreferencesQualifier),
                )
            }

            if (BuildConfig.DEBUG) {
                install(Logging) {
                    logger = Logger.ANDROID
                    level = LogLevel.BODY
                }
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
