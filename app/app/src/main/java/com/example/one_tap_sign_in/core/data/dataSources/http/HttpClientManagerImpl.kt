package com.example.one_tap_sign_in.core.data.dataSources.http

import com.example.one_tap_sign_in.core.data.exceptions.HttpException
import com.example.one_tap_sign_in.core.data.exceptions.toHttpException
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
            throw response.status.toHttpException()
        } catch (e: Exception) {
            if (e is HttpException) throw e

            // Handle Network & Serialization Errors
            throw e.toHttpException()
        }
    }
}
