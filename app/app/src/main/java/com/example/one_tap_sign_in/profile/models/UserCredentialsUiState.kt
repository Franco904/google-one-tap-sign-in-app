package com.example.one_tap_sign_in.profile.models

data class UserCredentialsUiState(
    val displayName: String? = null,
    val profilePictureUrl: String? = null,
) {
    fun isNull(): Boolean {
        return this.displayName == null
                || this.profilePictureUrl == null
    }
}
