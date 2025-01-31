package com.example.one_tap_sign_in.core.data.dataSources.http.requestDtos

import kotlinx.serialization.Serializable

@Serializable
data class SignInRequestDto(
    val idToken: String,
)
