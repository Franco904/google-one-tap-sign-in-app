package com.example.user.responseDtos

import kotlinx.serialization.Serializable

@Serializable
data class SignInResponseDto(
    val sessionId: String,
)
