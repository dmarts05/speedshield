package com.dmarts05.speedshield.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dmarts05.speedshield.data.service.TokenDataStoreService
import com.dmarts05.speedshield.data.service.impl.TokenDataStoreServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Singleton
    @Provides
    fun provideTokenDataStoreService(dataStore: DataStore<Preferences>): TokenDataStoreService {
        return TokenDataStoreServiceImpl(dataStore)
    }
}
