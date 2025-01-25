package com.example.user

import com.example.user.requestDtos.SignInRequestDto
import com.example.user.responseDtos.SignInResponseDto
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.Logger

const val USER_ROUTE = "/user"

fun Route.userRoutes(
    userService: UserService,
    logger: Logger,
) {
    route(USER_ROUTE) {
        post("/sign-in") {
            val signInRequest = call.receive<SignInRequestDto>()

            val sessionId = try {
                userService.startSession(idToken = signInRequest.idToken)
            } catch (e: Exception) {
                logger.error("-=- Token verification failed. -=-")
                throw e
            }

            logger.info("-=- Token verification succeded. -=-")

//            val session = UserSession(id = sessionId, name = "Franco")
//            call.sessions.set(name = session.name, value = session)

            call.respond(
                message = SignInResponseDto(sessionId = sessionId),
                status = HttpStatusCode.OK,
            )
        }
    }
}

//fun extractSessionId() {
//    val headers = call.request.headers
//    val hasAuthorizationHeader = headers.contains("Authorization")
//
//    if (hasAuthorizationHeader) {
//        val authHeader = headers
//            .entries()
//            .find { (key, _) -> key == "Authorization" }
//
//        val sessionId = authHeader?.value?.firstOrNull()?.substringAfter("Bearer")?.trim()
//    }
//}
