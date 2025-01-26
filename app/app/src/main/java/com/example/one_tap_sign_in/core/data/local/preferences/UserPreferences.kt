package com.example.one_tap_sign_in.core.data.local.preferences

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val sessionId: String? = null,
    val displayName: String? = null,
    val profilePictureUrl: String? = null,
)
