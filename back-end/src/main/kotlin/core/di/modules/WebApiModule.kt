package com.example.core.di.modules

import com.example.core.data.dataSources.authServer.GoogleClientApiImpl
import com.example.core.data.dataSources.authServer.interfaces.AuthClientApi
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.apache.v2.ApacheHttpTransport
import com.google.api.client.json.gson.GsonFactory
import io.ktor.server.application.*
import org.koin.dsl.module

val webApiModule = module {
    single<GoogleIdTokenVerifier> {
        val appConfig = get<Application>().environment.config

        val audience = appConfig.property("oauth2.audience").getString()

        GoogleIdTokenVerifier.Builder(
            ApacheHttpTransport(),
            GsonFactory.getDefaultInstance(),
        )
            .setAudience(listOf(audience))
            .build()
    }

    single<AuthClientApi> {
        GoogleClientApiImpl(
            googleIdTokenVerifier = get(),
        )
    }
}
