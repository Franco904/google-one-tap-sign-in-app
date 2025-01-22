package com.example.signIn

import com.example.core.security.session.UserSession
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.signInRoutes() {
    get("/sign-in/session-increment") {
        val session = call.sessions.get<UserSession>() ?: UserSession()
        call.sessions.set(session.copy(count = session.count + 1))

        call.respondText("Counter is ${session.count}. Refresh to increment.")
    }
}
