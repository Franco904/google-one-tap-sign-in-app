package com.example.core.data.entities

import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    val id: String,
    val email: String,
    val name: String,
    val profilePictureUrl: String,
)
