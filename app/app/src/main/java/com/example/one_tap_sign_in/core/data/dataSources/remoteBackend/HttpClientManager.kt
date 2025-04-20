package com.example.one_tap_sign_in.core.data.dataSources.remoteBackend

import com.example.one_tap_sign_in.core.domain.utils.AppResult
import com.example.one_tap_sign_in.core.domain.utils.DataSourceError
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod

interface HttpClientManager {
    suspend fun request(
        url: String,
        method: HttpMethod,
        body: Any? = null,
    ): AppResult<HttpResponse, DataSourceError.RemoteBackendError>
}
