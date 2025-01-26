package com.example.one_tap_sign_in.core.data.remote.requestDtos

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequestDto(
    val name: String,
)
