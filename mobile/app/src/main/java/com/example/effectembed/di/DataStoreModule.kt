package com.example.effectembed.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.example.effectembed.auth.model.UserInfo
import com.example.effectembed.auth.model.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideUserDataStore(@ApplicationContext context: Context): DataStore<UserInfo> {
        return context.dataStore
    }
}