package com.example.one_tap_sign_in.core.data.dataSources.remoteBackend

import com.example.one_tap_sign_in.core.data.exceptions.RemoteBackendException
import com.example.one_tap_sign_in.core.data.exceptions.asRemoteBackendException
import io.ktor.client.HttpClient
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod

class HttpClientManagerImpl(
    private val client: HttpClient,
) : HttpClientManager {
    override suspend fun request(
        url: String,
        method: HttpMethod,
        body: Any?,
    ): HttpResponse {
        try {
            val response = client.request(url) {
                this.method = method
                if (body != null) setBody(body)
            }

            if (response.status.value in 200..299) {
                return response
            }

            // Handle standard HTTP errors
            throw response.status.asRemoteBackendException()
        } catch (e: Exception) {
            if (e is RemoteBackendException) throw e

            // Handle Network & Serialization Errors
            throw e.asRemoteBackendException()
        }
    }
}
