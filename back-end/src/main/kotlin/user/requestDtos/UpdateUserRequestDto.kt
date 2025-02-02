package com.example.user.requestDtos

import com.example.core.domain.models.User
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequestDto(
    val name: String,
) {
    fun toUser() = User(name = name)
}
