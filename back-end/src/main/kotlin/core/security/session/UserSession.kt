package com.example.core.security.session

import com.example.core.constants.SESSION_EXPIRATION_TIME_MILLIS
import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val id: String,
    val name: String,
    val expirationTimestamp: Long = System.currentTimeMillis() + SESSION_EXPIRATION_TIME_MILLIS,
)
