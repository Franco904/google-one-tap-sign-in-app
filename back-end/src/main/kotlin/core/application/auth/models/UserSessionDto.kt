package com.example.core.application.auth.models

import com.example.core.data.constants.SESSION_EXPIRATION_TIME_MILLIS
import com.example.core.domain.models.UserSession
import kotlinx.serialization.Serializable

@Serializable
data class UserSessionDto(
    val id: String,
    val name: String,
    val expirationTimestamp: Long = System.currentTimeMillis() + SESSION_EXPIRATION_TIME_MILLIS,
) {
    fun toUserSession() = UserSession(id = id)
}
