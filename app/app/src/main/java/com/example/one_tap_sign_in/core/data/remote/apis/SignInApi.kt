package com.example.one_tap_sign_in.core.data.remote.apis

import retrofit2.http.GET
import retrofit2.http.Header

interface SignInApi {
    @GET("/sign-in")
    suspend fun signInUser(
        @Header("Authorization") authorization: String,
    )
}
