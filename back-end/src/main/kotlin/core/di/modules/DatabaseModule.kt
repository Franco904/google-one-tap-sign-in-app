package com.example.core.di.modules

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import org.koin.dsl.module

val databaseModule = module {
    single<MongoClient> {
        val mongoUri = "ktor.mongo.uri"

        MongoClient.create(connectionString = mongoUri)
    }

    single<MongoDatabase> {
        val client = get<MongoClient>()
        val databaseName = "ktor.mongo.database"

        client.getDatabase(databaseName = databaseName)
    }
}
