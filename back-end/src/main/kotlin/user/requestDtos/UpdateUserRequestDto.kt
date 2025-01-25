package com.example.user.requestDtos

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequestDto(
    val name: String,
)
