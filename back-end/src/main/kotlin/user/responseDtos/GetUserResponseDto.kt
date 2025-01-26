package com.example.user.responseDtos

import kotlinx.serialization.Serializable

@Serializable
data class GetUserResponseDto(
    val email: String,
    val name: String,
    val profilePictureUrl: String,
)
