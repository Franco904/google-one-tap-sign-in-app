package com.example.user.requestDtos

import kotlinx.serialization.Serializable

@Serializable
data class UpdateRequestDto(
    val name: String,
)
