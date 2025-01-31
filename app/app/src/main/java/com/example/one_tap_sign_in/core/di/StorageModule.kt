package com.example.one_tap_sign_in.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.example.one_tap_sign_in.core.data.dataSources.preferences.UserPreferences
import com.example.one_tap_sign_in.core.data.dataSources.preferences.UserPreferencesStorageImpl
import com.example.one_tap_sign_in.core.data.dataSources.preferences.UserPreferencesStorageImpl.Companion.dataStore
import com.example.one_tap_sign_in.core.data.dataSources.preferences.interfaces.UserPreferencesStorage
import org.koin.dsl.module

val storageModule = module {
    single<DataStore<UserPreferences>> { get<Context>().dataStore }
    single<UserPreferencesStorage> { UserPreferencesStorageImpl(dataStore = get()) }
}
