package com.example.user.requestDtos

import kotlinx.serialization.Serializable

@Serializable
data class SignInRequestDto(
    val idToken: String,
)
