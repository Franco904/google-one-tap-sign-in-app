package com.example.one_tap_sign_in.core.application.navigation

import kotlinx.serialization.Serializable

sealed interface Destinations {
    @Serializable
    data object Root

    @Serializable
    data object SignIn

    @Serializable
    data object Profile
}
