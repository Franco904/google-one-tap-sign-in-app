package com.example.one_tap_sign_in.core.data.dataSources.http.apis

import com.example.one_tap_sign_in.core.data.dataSources.http.HttpClientManager
import com.example.one_tap_sign_in.core.data.dataSources.http.apis.interfaces.UserApi
import com.example.one_tap_sign_in.core.data.dataSources.http.requestDtos.SignInRequestDto
import com.example.one_tap_sign_in.core.data.dataSources.http.requestDtos.UpdateUserRequestDto
import com.example.one_tap_sign_in.core.data.dataSources.http.responseDtos.GetUserResponseDto
import io.ktor.client.call.body
import io.ktor.http.HttpMethod

class UserApiImpl(
    private val client: HttpClientManager,
) : UserApi {
    override suspend fun signInUser(signInRequestDto: SignInRequestDto) {
        client.request(
            url = "$ROUTE/sign-in",
            method = HttpMethod.Post,
            body = signInRequestDto,
        )
    }

    override suspend fun getUser(): GetUserResponseDto {
        return client.request(
            url = ROUTE,
            method = HttpMethod.Get,
        ).body()
    }

    override suspend fun updateUser(updateUserRequestDto: UpdateUserRequestDto) {
        client.request(
            url = "$ROUTE/update",
            method = HttpMethod.Put,
            body = updateUserRequestDto,
        )
    }

    override suspend fun deleteUser() {
        client.request(
            url = "$ROUTE/delete",
            method = HttpMethod.Delete,
        )
    }

    override suspend fun signOutUser() {
        client.request(
            url = "$ROUTE/sign-out",
            method = HttpMethod.Get,
        )
    }

    companion object {
        private const val ROUTE = "/user"
    }
}
