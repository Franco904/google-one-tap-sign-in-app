package com.example.one_tap_sign_in.core.data.dataSources.preferences.plain

import kotlinx.serialization.Serializable

@Serializable
data class PlainPreferences(
    val didUserExplicitlySignOut: Boolean? = null,
)
