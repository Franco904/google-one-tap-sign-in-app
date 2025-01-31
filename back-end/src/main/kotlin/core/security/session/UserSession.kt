package com.example.core.security.session

import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val id: String,
    val name: String,
    val expirationTimestamp: Long = System.currentTimeMillis() + 10000L,
)
