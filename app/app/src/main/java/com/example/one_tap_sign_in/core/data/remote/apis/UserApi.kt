package com.example.one_tap_sign_in.core.data.remote.apis

import com.example.one_tap_sign_in.core.data.remote.requestDtos.SignInRequestDto
import com.example.one_tap_sign_in.core.data.remote.requestDtos.UpdateUserRequestDto
import com.example.one_tap_sign_in.core.data.remote.responseDtos.GetUserResponseDto
import com.example.one_tap_sign_in.core.data.remote.responseDtos.SignInResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserApi {
    @POST("$ROUTE/sign-in")
    suspend fun signInUser(
        @Body signInRequestDto: SignInRequestDto,
    ): SignInResponseDto

    @GET(ROUTE)
    suspend fun getUser(): GetUserResponseDto

    @PUT("$ROUTE/update")
    suspend fun updateUser(
        @Body updateUserRequestDto: UpdateUserRequestDto,
    ): GetUserResponseDto

    @DELETE("$ROUTE/delete")
    suspend fun deleteUser()

    @GET("$ROUTE/sign-out")
    suspend fun signOutUser()

    companion object {
        private const val ROUTE = "/user"
    }
}
