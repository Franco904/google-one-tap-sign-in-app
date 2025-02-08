package com.example.one_tap_sign_in.core.data.dataSources.preferences.encrypted

import com.example.one_tap_sign_in.core.domain.models.User
import io.ktor.http.Cookie
import kotlinx.serialization.Serializable

@Serializable
data class EncryptedPreferences(
    val sessionCookie: Cookie? = null,
    val displayName: String? = null,
    val profilePictureUrl: String? = null,
    val isUserEditSynced: Boolean = true,
) {
    fun toUser(): User {
        return User(
            email = null,
            name = displayName,
            profilePictureUrl = profilePictureUrl,
            isUserModifySynced = isUserEditSynced,
        )
    }
}
