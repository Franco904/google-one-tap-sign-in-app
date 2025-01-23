package com.example.core.di.modules

import com.example.core.data.daos.UserDaoImpl
import com.example.core.data.daos.apis.UserDao
import org.koin.dsl.module

val daoModule = module {
    single<UserDao> {
        UserDaoImpl(
            database = get(),
        )
    }
}
