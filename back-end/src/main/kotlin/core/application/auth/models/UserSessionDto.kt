package com.example.core.application.auth.models

import com.example.core.domain.models.UserSession
import kotlinx.serialization.Serializable

@Serializable
data class UserSessionDto(
    val id: String,
    val name: String,
    val expirationTimestamp: Long,
) {
    fun toUserSession() = UserSession(id = id)
}
