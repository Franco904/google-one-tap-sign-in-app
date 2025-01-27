package com.example.one_tap_sign_in.core.infra.auth

import com.example.one_tap_sign_in.core.constants.SESSION_COOKIE_NAME
import com.example.one_tap_sign_in.core.data.local.preferences.UserPreferencesStorage
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import kotlinx.coroutines.flow.first

class AppCookieStorage(
    private val userPreferencesStorage: UserPreferencesStorage,
) : CookiesStorage {
    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        if (cookie.name == SESSION_COOKIE_NAME) {
            userPreferencesStorage.savePreferences { prefs ->
                prefs.copy(sessionCookie = cookie)
            }
        }
    }

    override suspend fun get(requestUrl: Url): List<Cookie> {
        val sessionCookie = userPreferencesStorage.readPreferences().first().sessionCookie

        return if (sessionCookie == null) emptyList() else listOf(sessionCookie)
    }

    override fun close() {}
}