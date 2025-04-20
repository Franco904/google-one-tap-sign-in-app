package com.example.one_tap_sign_in.core.data.dataSources.remoteBackend.apis.interfaces

import com.example.one_tap_sign_in.core.data.dataSources.remoteBackend.requestDtos.SignInRequestDto
import com.example.one_tap_sign_in.core.data.dataSources.remoteBackend.requestDtos.UpdateUserRequestDto
import com.example.one_tap_sign_in.core.data.dataSources.remoteBackend.responseDtos.GetUserResponseDto
import com.example.one_tap_sign_in.core.domain.utils.AppResult
import com.example.one_tap_sign_in.core.domain.utils.DataSourceError

interface UserApi {
    suspend fun signInUser(
        signInRequestDto: SignInRequestDto,
    ): AppResult<Unit, DataSourceError.RemoteBackendError>

    suspend fun getUser(): AppResult<GetUserResponseDto, DataSourceError.RemoteBackendError>

    suspend fun updateUser(
        updateUserRequestDto: UpdateUserRequestDto,
    ): AppResult<Unit, DataSourceError.RemoteBackendError>

    suspend fun deleteUser(): AppResult<Unit, DataSourceError.RemoteBackendError>

    suspend fun signOutUser(): AppResult<Unit, DataSourceError.RemoteBackendError>
}
