package com.example.core.presentation.auth.models

import com.example.core.data.constants.SESSION_EXPIRATION_TIME_MILLIS
import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val id: String,
    val name: String,
    val expirationTimestamp: Long = System.currentTimeMillis() + SESSION_EXPIRATION_TIME_MILLIS,
)
