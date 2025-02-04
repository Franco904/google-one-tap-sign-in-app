package com.example.one_tap_sign_in.core.application.di.qualifiers

import org.koin.core.qualifier.named

val encryptedPreferencesQualifier = named("EncryptedPreferences")
val plainPreferencesQualifier = named("PlainPreferences")
