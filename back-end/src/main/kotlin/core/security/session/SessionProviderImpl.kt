package com.example.core.security.session

import com.example.core.data.entities.UserEntity
import com.example.core.security.session.interfaces.SessionProvider

class SessionProviderImpl : SessionProvider {
    override suspend fun generateSessionId(user: UserEntity): String {
        return "esjhdfoiusdf890sdhjfsd09uad809fhandiof"
    }
}
