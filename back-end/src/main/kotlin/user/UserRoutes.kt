package com.example.user

import com.example.core.constants.SESSION_NAME
import com.example.core.security.session.UserSession
import com.example.user.requestDtos.SignInRequestDto
import com.example.user.responseDtos.SignInResponseDto
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

const val USER_ROUTE = "/user"

fun Route.userRoutes(
    userService: UserService,
) {
    route(USER_ROUTE) {
        post("/sign-in") {
            val signInRequest = call.receive<SignInRequestDto>()

            val (userSub, userName) = userService.startSession(idToken = signInRequest.idToken)

            val session = UserSession(id = userSub, name = userName)
            call.sessions.set(name = SESSION_NAME, value = session)

            call.respond(
                message = SignInResponseDto(sessionId = session.id),
                status = HttpStatusCode.OK,
            )
        }
    }
}
