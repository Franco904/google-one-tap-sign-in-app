package com.example.core.security.session.interfaces

import com.example.core.data.entities.UserEntity

interface SessionProvider {
    suspend fun generateSessionId(user: UserEntity): String
}
