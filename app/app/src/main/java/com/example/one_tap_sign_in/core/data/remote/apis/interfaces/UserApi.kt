package com.example.one_tap_sign_in.core.data.remote.apis.interfaces

import com.example.one_tap_sign_in.core.data.remote.requestDtos.SignInRequestDto
import com.example.one_tap_sign_in.core.data.remote.requestDtos.UpdateUserRequestDto
import com.example.one_tap_sign_in.core.data.remote.responseDtos.GetUserResponseDto

interface UserApi {
    suspend fun signInUser(signInRequestDto: SignInRequestDto)

    suspend fun getUser(): GetUserResponseDto

    suspend fun updateUser(updateUserRequestDto: UpdateUserRequestDto)

    suspend fun deleteUser()

    suspend fun signOutUser()
}
