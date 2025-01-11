package com.example.one_tap_sign_in.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkApiModule {
    @Provides
    @Singleton
    fun provideHttpClient() {}
}
