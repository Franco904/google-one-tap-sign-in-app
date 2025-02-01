package com.example.core.di.modules

import com.example.core.data.dataSources.database.daos.UserDaoImpl
import com.example.core.data.dataSources.database.daos.interfaces.UserDao
import org.koin.dsl.module

val daoModule = module {
    single<UserDao> {
        UserDaoImpl(
            database = get(),
        )
    }
}
