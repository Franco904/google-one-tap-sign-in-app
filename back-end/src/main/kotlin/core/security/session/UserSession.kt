package com.example.core.security.session

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val id: String,
    val name: String,
) : Principal

