package com.example.one_tap_sign_in.core.application.di.modules

import android.content.Context
import androidx.datastore.core.DataStore
import com.example.one_tap_sign_in.core.data.dataSources.preferences.EncryptedPreferences
import com.example.one_tap_sign_in.core.data.dataSources.preferences.EncryptedPreferencesStorageImpl
import com.example.one_tap_sign_in.core.data.dataSources.preferences.EncryptedPreferencesStorageImpl.Companion.encryptedDataStore
import com.example.one_tap_sign_in.core.data.dataSources.preferences.interfaces.EncryptedPreferencesStorage
import org.koin.dsl.module

val storageModule = module {
    single<DataStore<EncryptedPreferences>> { get<Context>().encryptedDataStore }
    single<EncryptedPreferencesStorage> { EncryptedPreferencesStorageImpl(dataStore = get()) }
}
