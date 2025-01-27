package com.example.user.responseDtos

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserResponseDto(
    val message: String?,
)
