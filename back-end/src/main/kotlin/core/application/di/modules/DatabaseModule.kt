package com.example.core.application.di.modules

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.server.application.*
import org.koin.dsl.module

val databaseModule = module {
    single<MongoClient> {
        val appConfig = get<Application>().environment.config
        val mongoUri = appConfig.property("mongodb.database_server_uri").getString()

        MongoClient.create(connectionString = mongoUri)
    }

    single<MongoDatabase> {
        val appConfig = get<Application>().environment.config
        val client = get<MongoClient>()

        val databaseName = appConfig.property("mongodb.database_name").getString()

        client.getDatabase(databaseName = databaseName)
    }
}
