package com.example.user.requestDtos

import kotlinx.serialization.Serializable

@Serializable
data class SignInUserRequestDto(
    val idToken: String,
)
