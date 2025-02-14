package com.example.one_tap_sign_in.core.data.dataSources.cookieStorage

import android.util.Log
import com.example.one_tap_sign_in.core.data.constants.SESSION_COOKIE_NAME
import com.example.one_tap_sign_in.core.data.dataSources.preferences.encrypted.EncryptedPreferences
import com.example.one_tap_sign_in.core.data.dataSources.preferences.interfaces.PreferencesStorage
import com.example.one_tap_sign_in.core.data.exceptions.asPreferencesException
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import kotlinx.coroutines.flow.first

class AppCookieStorage(
    private val encryptedPreferencesStorage: PreferencesStorage<EncryptedPreferences>,
) : CookiesStorage {
    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        try {
            if (cookie.name == SESSION_COOKIE_NAME) {
                encryptedPreferencesStorage.savePreferences { prefs ->
                    prefs.copy(sessionCookie = cookie)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "${e.asPreferencesException().message}")
            throw e
        }
    }

    override suspend fun get(requestUrl: Url): List<Cookie> {
        try {
            val sessionCookie = encryptedPreferencesStorage.readPreferences().first().sessionCookie

            return if (sessionCookie == null) emptyList() else listOf(sessionCookie)
        } catch (e: Exception) {
            Log.e(TAG, "${e.asPreferencesException().message}")
            throw e
        }
    }

    override fun close() {}

    companion object {
        private const val TAG = "AppCookieStorage"
    }
}
