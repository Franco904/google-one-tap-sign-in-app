package com.example.one_tap_sign_in.core.data.dataSources.http.apis.interfaces

import com.example.one_tap_sign_in.core.data.dataSources.http.requestDtos.SignInRequestDto
import com.example.one_tap_sign_in.core.data.dataSources.http.requestDtos.UpdateUserRequestDto
import com.example.one_tap_sign_in.core.data.dataSources.http.responseDtos.GetUserResponseDto

interface UserApi {
    suspend fun signInUser(signInRequestDto: SignInRequestDto)

    suspend fun getUser(): GetUserResponseDto

    suspend fun updateUser(updateUserRequestDto: UpdateUserRequestDto)

    suspend fun deleteUser()

    suspend fun signOutUser()
}
