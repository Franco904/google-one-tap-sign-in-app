package com.example.user

import com.example.core.exceptionHandling.exceptions.UnauthorizedException
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

const val USER_ROUTE = "/user"

fun Route.userRoutes() {
    val userService by inject<UserService>()

    route(USER_ROUTE) {
        get("/sign-in") {
            val headers = call.request.headers
            val hasAuthorizationHeader = headers.contains("Authorization")

            if (hasAuthorizationHeader) {
                val authHeader = headers
                    .entries()
                    .find { (key, _) -> key == "Authorization" }

                val idToken = authHeader?.value?.firstOrNull()?.substringAfter("Bearer")?.trim()

//                if (idToken != null) userService.signInWithGoogle(idToken = idToken)
            }

            call.respondText(
                text = "$hasAuthorizationHeader",
                status = HttpStatusCode.OK,
            )
        }
    }
}
