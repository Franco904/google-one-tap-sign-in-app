package com.example.user.responseDtos

import kotlinx.serialization.Serializable

@Serializable
data class SignInUserResponseDto(
    val sessionId: String,
)
