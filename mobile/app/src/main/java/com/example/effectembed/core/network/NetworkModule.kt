package com.example.effectembed.core.network

import com.example.effectembed.stego.model.StegoApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideStegoApiService(): StegoApiService {
        return ApiClient.retrofitService
    }
}