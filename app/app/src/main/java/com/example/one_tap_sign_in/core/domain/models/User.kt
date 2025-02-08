package com.example.one_tap_sign_in.core.domain.models

data class User(
    val email: String? = null,
    val name: String? = null,
    val profilePictureUrl: String? = null,
    val isUserModifySynced: Boolean? = null,
) {
    fun isNull() = this == User(
        email = null,
        name = null,
        profilePictureUrl = null,
    )
}
