package com.example.one_tap_sign_in.core.data.remote.apis

import retrofit2.http.GET

interface SignInApi {
    @GET("/sign-in")
    suspend fun signInUser()
}
