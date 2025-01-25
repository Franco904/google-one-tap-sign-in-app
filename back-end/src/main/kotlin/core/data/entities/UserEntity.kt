package com.example.core.data.entities

import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    val email: String? = null,
    val name: String? = null,
    val profilePictureUrl: String? = null,
)
