package com.example.one_tap_sign_in.signin.models

data class GoogleUserCredentials(
    val idToken: String,
    val displayName: String? = null,
    val profilePictureUrl: String? = null,
)
