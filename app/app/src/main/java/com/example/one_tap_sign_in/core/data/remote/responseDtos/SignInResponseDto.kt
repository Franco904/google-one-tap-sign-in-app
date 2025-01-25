package com.example.one_tap_sign_in.core.data.remote.responseDtos

import kotlinx.serialization.Serializable

@Serializable
data class SignInResponseDto(
    val sessionId: String,
)
