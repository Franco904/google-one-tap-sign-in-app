package com.example.one_tap_sign_in.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.example.one_tap_sign_in.core.data.local.preferences.DataStoreUserPreferencesStorage
import com.example.one_tap_sign_in.core.data.local.preferences.DataStoreUserPreferencesStorage.Companion.dataStore
import com.example.one_tap_sign_in.core.data.local.preferences.UserPreferences
import com.example.one_tap_sign_in.core.data.local.preferences.UserPreferencesStorage
import org.koin.dsl.module

val storageModule = module {
    single<DataStore<UserPreferences>> { get<Context>().dataStore }
    single<UserPreferencesStorage> { DataStoreUserPreferencesStorage(dataStore = get()) }
}
