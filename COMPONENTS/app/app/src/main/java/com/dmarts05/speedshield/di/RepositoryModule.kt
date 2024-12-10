package com.dmarts05.speedshield.di

import com.dmarts05.speedshield.data.network.AuthApi
import com.dmarts05.speedshield.data.repository.AuthRepository
import com.dmarts05.speedshield.data.repository.impl.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Authenticator
    @Singleton
    @Provides
    fun provideAuthenticatorAuthRepository(
        @Authenticator authenticatorAuthApi: AuthApi,
    ): AuthRepository {
        return AuthRepositoryImpl(authenticatorAuthApi)
    }

    @Singleton
    @Provides
    fun provideAuthRepository(
        authApi: AuthApi,
    ): AuthRepository {
        return AuthRepositoryImpl(authApi)
    }
}
