package com.example.effectembed.di

import androidx.datastore.core.DataStore
import com.example.effectembed.auth.model.AuthRepository
import com.example.effectembed.auth.model.IAuthRepository
import com.example.effectembed.auth.model.RemoteAuthDataSource
import com.example.effectembed.auth.model.SessionManager
import com.example.effectembed.auth.model.UserInfo
import com.example.effectembed.stego.model.IStegoRepository
import com.example.effectembed.stego.model.RemoteStegoDataSource
import com.example.effectembed.stego.model.StegoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(dataSource: RemoteAuthDataSource, dataStore: DataStore<UserInfo>): IAuthRepository {
        return AuthRepository(dataSource, dataStore)
    }
    @Provides
    @Singleton
    fun provideStegoRepository(dataSource: RemoteStegoDataSource, sessionManager: SessionManager): IStegoRepository {
        return StegoRepository(dataSource, sessionManager)
    }
}