package com.example.one_tap_sign_in.core.data.remote.apis

import com.example.one_tap_sign_in.core.data.remote.requestDtos.SignInRequestDto
import com.example.one_tap_sign_in.core.data.remote.responseDtos.SignInResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("$ROUTE/sign-in")
    suspend fun signInUser(
        @Body signInRequestDto: SignInRequestDto,
    ): SignInResponseDto

    companion object {
        private const val ROUTE = "/user"
    }
}
