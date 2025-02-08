package com.example.one_tap_sign_in.core.data.dataSources.remoteBackend

import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod

interface HttpClientManager {
    suspend fun request(
        url: String,
        method: HttpMethod,
        body: Any? = null,
    ): HttpResponse
}
