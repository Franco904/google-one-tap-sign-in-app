package com.example.one_tap_sign_in.core.data.local.preferences

import androidx.datastore.core.Serializer
import com.example.one_tap_sign_in.core.utils.CryptoUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object UserPreferencesSerializer : Serializer<UserPreferences> {
    override val defaultValue: UserPreferences
        get() = UserPreferences()

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
        val preferencesJson = Json.encodeToString(t)
        val preferencesBytes = preferencesJson.toByteArray()

        val preferencesEncryptedBytes = CryptoUtils().encrypt(preferencesBytes)

        withContext(Dispatchers.IO) {
            output.use { it.write(preferencesEncryptedBytes) }
        }
    }

    override suspend fun readFrom(input: InputStream): UserPreferences {
        val preferencesEncryptedBytes = withContext(Dispatchers.IO) {
            input.use { it.readBytes() }
        }

        val preferencesDecryptedBytes = CryptoUtils().decrypt(preferencesEncryptedBytes)

        val preferences = Json.decodeFromString<UserPreferences>(
            string = preferencesDecryptedBytes.decodeToString(),
        )

        return preferences
    }
}
