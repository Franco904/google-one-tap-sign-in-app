package com.example.signIn

import com.example.core.security.session.UserSession
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject

fun Route.signInRoutes() {
    val signInService by inject<SignInService>()

    route("/sign-in") {
        get {
            val headers = call.request.headers
            val hasAuthorizationHeader = headers.contains("Authorization")

            signInService.signInWithGoogle()

            call.respondText("$hasAuthorizationHeader")
        }

        get("/session-increment") {
            val session = call.sessions.get<UserSession>() ?: UserSession()
            call.sessions.set(session.copy(count = session.count + 1))

            call.respondText("Counter is ${session.count}. Refresh to increment.")
        }
    }
}
