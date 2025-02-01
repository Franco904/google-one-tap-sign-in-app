package com.example.one_tap_sign_in.profile.models

import com.example.one_tap_sign_in.core.data.models.User

data class UserCredentialsUiState(
    val displayName: String? = null,
    val profilePictureUrl: String? = null,
) {
    fun isNull(): Boolean {
        return this.displayName == null
                || this.profilePictureUrl == null
    }

    fun toUser(): User = User(
        email = null,
        name = displayName,
        profilePictureUrl = profilePictureUrl,
    )
}
