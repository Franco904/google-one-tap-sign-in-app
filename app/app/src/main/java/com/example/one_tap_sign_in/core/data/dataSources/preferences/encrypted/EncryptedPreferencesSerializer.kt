package com.example.one_tap_sign_in.core.data.dataSources.preferences.encrypted

import android.util.Log
import androidx.datastore.core.Serializer
import com.example.one_tap_sign_in.core.data.exceptions.toPreferencesException
import com.example.one_tap_sign_in.core.data.utils.CryptoUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object EncryptedPreferencesSerializer : Serializer<EncryptedPreferences> {
    private const val TAG = "EncryptedPreferencesSerializer"

    override val defaultValue: EncryptedPreferences
        get() = EncryptedPreferences()

    override suspend fun writeTo(t: EncryptedPreferences, output: OutputStream) {
        try {
            val preferencesJson = Json.encodeToString(t)
            val preferencesBytes = preferencesJson.toByteArray()

            val preferencesEncryptedBytes = CryptoUtils.encrypt(preferencesBytes)

            withContext(Dispatchers.IO) {
                output.use { it.write(preferencesEncryptedBytes) }
            }
        } catch (e: Exception) {
            Log.e(TAG, "${e.toPreferencesException().message}")
        }
    }

    override suspend fun readFrom(input: InputStream): EncryptedPreferences {
        return try {
            val preferencesEncryptedBytes = withContext(Dispatchers.IO) {
                input.use { it.readBytes() }
            }

            val preferencesDecryptedBytes = CryptoUtils.decrypt(preferencesEncryptedBytes)

            val preferences = Json.decodeFromString<EncryptedPreferences>(
                string = preferencesDecryptedBytes.decodeToString(),
            )

            preferences
        } catch (e: Exception) {
            Log.e(TAG, "${e.toPreferencesException().message}")
            defaultValue
        }
    }
}
