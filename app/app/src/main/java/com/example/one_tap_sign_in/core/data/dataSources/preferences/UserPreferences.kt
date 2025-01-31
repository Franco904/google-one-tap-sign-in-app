package com.example.one_tap_sign_in.core.data.dataSources.preferences

import io.ktor.http.Cookie
import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val sessionCookie: Cookie? = null,
    val displayName: String? = null,
    val profilePictureUrl: String? = null,
)
