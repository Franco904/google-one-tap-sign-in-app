package com.example.user

import com.example.core.application.auth.models.UserSessionDto
import com.example.core.application.constants.SESSION_COOKIE_NAME
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

            val userSession = userService.createUserSession(idToken = signInRequest.idToken)
            call.sessions.set(name = SESSION_COOKIE_NAME, value = userSession)

            call.respond(message = HttpStatusCode.OK)
        }

        authenticate(SESSION_COOKIE_NAME) {
            get {
                val userSessionDto = call.principal<UserSessionDto>()
                val user = userService.getUser(userSessionDto = userSessionDto)

                call.respond(
                    message = user,
                    status = HttpStatusCode.OK,
                )
            }

            put("/update") {
                val userSessionDto = call.principal<UserSessionDto>()
                val userDto = call.receive<UpdateUserRequestDto>()

                userService.updateUser(
                    userSessionDto = userSessionDto,
                    userDto = userDto,
                )

                call.respond(
                    message = UpdateUserResponseDto(message = "User successfully updated."),
                    status = HttpStatusCode.OK,
                )
            }

            delete("/delete") {
                val userSessionDto = call.principal<UserSessionDto>()

                userService.deleteUser(userSessionDto = userSessionDto)

                call.respond(message = HttpStatusCode.OK)
            }

            get("/sign-out") {
                call.sessions.clear<UserSessionDto>()

                call.respond(message = HttpStatusCode.OK)
            }
        }
    }
}
