package com.example.one_tap_sign_in.profile.models

import com.example.one_tap_sign_in.core.domain.models.User

data class UserCredentialsUiState(
    val displayName: String? = null,
    val profilePictureUrl: String? = null,
) {
    fun toUser() = User(
        email = null,
        name = displayName,
        profilePictureUrl = profilePictureUrl,
    )

    companion object {
        fun fromUser(user: User): UserCredentialsUiState {
            return UserCredentialsUiState(
                displayName = user.name,
                profilePictureUrl = user.profilePictureUrl,
            )
        }
    }
}
