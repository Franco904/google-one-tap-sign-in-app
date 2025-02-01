package com.example.user

import com.example.core.data.constants.SESSION_COOKIE_NAME
import com.example.core.presentation.auth.models.UserSession
import com.example.user.requestDtos.SignInUserRequestDto
import com.example.user.requestDtos.UpdateUserRequestDto
import com.example.user.responseDtos.UpdateUserResponseDto
import io.ktor.http.*
import io.ktor.server.auth.*
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
            val signInRequest = call.receive<SignInUserRequestDto>()

            val session = userService.createUserSession(idToken = signInRequest.idToken)
            call.sessions.set(name = SESSION_COOKIE_NAME, value = session)

            call.respond(
                message = true,
                status = HttpStatusCode.OK,
            )
        }

        authenticate(SESSION_COOKIE_NAME) {
            get {
                val session = call.principal<UserSession>()
                val user = userService.getUser(session = session)

                call.respond(
                    message = user,
                    status = HttpStatusCode.OK,
                )
            }

            put("/update") {
                val session = call.principal<UserSession>()
                val updateBody = call.receive<UpdateUserRequestDto>()

                userService.updateUser(
                    session = session,
                    user = updateBody,
                )

                call.respond(
                    message = UpdateUserResponseDto(message = "User successfully updated."),
                    status = HttpStatusCode.OK,
                )
            }

            delete("/delete") {
                val session = call.principal<UserSession>()

                userService.deleteUser(session = session)

                call.respond(
                    message = true,
                    status = HttpStatusCode.OK,
                )
            }

            get("/sign-out") {
                call.sessions.clear<UserSession>()

                call.respond(
                    message = true,
                    status = HttpStatusCode.OK,
                )
            }
        }
    }
}
