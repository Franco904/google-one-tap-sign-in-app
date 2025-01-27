package com.example.one_tap_sign_in.core.data.remote.apis

import com.example.one_tap_sign_in.core.data.remote.apis.interfaces.UserApi
import com.example.one_tap_sign_in.core.data.remote.requestDtos.SignInRequestDto
import com.example.one_tap_sign_in.core.data.remote.requestDtos.UpdateUserRequestDto
import com.example.one_tap_sign_in.core.data.remote.responseDtos.GetUserResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class UserApiImpl(
    private val client: HttpClient,
) : UserApi {
    override suspend fun signInUser(signInRequestDto: SignInRequestDto) {
        client.post("$ROUTE/sign-in") {
            setBody(signInRequestDto)
        }
    }

    override suspend fun getUser(): GetUserResponseDto {
        return client.get(ROUTE).body()
    }

    override suspend fun updateUser(updateUserRequestDto: UpdateUserRequestDto) {
        client.put("$ROUTE/update") {
            setBody(updateUserRequestDto)
        }
    }

    override suspend fun deleteUser() {
        client.delete("$ROUTE/delete")
    }

    override suspend fun signOutUser() {
        client.get("$ROUTE/sign-out")
    }

    companion object {
        private const val ROUTE = "/user"
    }
}
