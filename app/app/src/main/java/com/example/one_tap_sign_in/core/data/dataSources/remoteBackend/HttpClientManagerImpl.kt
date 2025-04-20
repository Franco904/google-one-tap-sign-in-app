package com.example.one_tap_sign_in.core.data.dataSources.remoteBackend

import android.util.Log
import com.example.one_tap_sign_in.BuildConfig
import com.example.one_tap_sign_in.core.data.exceptions.RemoteBackendException
import com.example.one_tap_sign_in.core.data.exceptions.asRemoteBackendException
import com.example.one_tap_sign_in.core.domain.utils.AppResult
import com.example.one_tap_sign_in.core.domain.utils.DataSourceError
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
    ): AppResult<HttpResponse, DataSourceError.RemoteBackendError> {
        try {
            val response = client.request(url) {
                this.method = method
                if (body != null) setBody(body)
            }

            if (response.status.value in 200..299) {
                return AppResult.Success(data = response)
            }

            // Handle standard HTTP errors
            throw response.status.asRemoteBackendException()
        } catch (e: Exception) {
            if (e is RemoteBackendException) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "request http error - ${e.message}")
                    e.printStackTrace()
                }

                return AppResult.Error(error = e.toRemoteBackendError())
            }

            // Handle Network & Serialization Errors
            val remoteBackendException = e.asRemoteBackendException()

            if (BuildConfig.DEBUG) {
                Log.e(TAG, "request error - ${remoteBackendException.message}")
                e.printStackTrace()
            }

            return AppResult.Error(error = remoteBackendException.toRemoteBackendError())
        }
    }

    companion object {
        private const val TAG = "HttpClientManagerImpl"
    }
}
