package com.example.one_tap_sign_in

import kotlinx.serialization.Serializable

sealed interface Destinations {
    @Serializable
    data object SignIn
}
