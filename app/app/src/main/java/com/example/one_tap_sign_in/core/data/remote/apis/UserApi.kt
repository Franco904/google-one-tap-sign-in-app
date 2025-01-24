package com.example.one_tap_sign_in.core.data.remote.apis

import retrofit2.http.GET
import retrofit2.http.Header

interface UserApi {
    @GET("$ROUTE/sign-in")
    suspend fun signInUser(
        @Header("Authorization") authorization: String,
    )

    companion object {
        private const val ROUTE = "/user"
    }
}
