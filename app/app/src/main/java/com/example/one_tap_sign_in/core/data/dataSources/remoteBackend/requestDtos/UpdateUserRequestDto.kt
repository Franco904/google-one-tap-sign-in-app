package com.example.one_tap_sign_in.core.data.dataSources.remoteBackend.requestDtos

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequestDto(
    val name: String,
)
