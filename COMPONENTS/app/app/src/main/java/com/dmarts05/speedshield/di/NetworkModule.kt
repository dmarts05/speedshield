package com.dmarts05.speedshield.di

import com.dmarts05.speedshield.data.network.AuthApi
import com.dmarts05.speedshield.data.network.AuthInterceptor
import com.dmarts05.speedshield.data.network.TokenAuthenticator
import com.dmarts05.speedshield.data.repository.AuthRepository
import com.dmarts05.speedshield.data.service.TokenDataStoreService
import com.dmarts05.speedshield.util.NetworkConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideAuthInterceptor(tokenDataStoreService: TokenDataStoreService): AuthInterceptor {
        return AuthInterceptor(tokenDataStoreService)
    }

    @Authenticator
    @Singleton
    @Provides
    fun provideAuthenticatorOkHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .connectTimeout(NetworkConstants.TIMEOUT_DURATION_SECONDS, TimeUnit.SECONDS)
            .readTimeout(NetworkConstants.TIMEOUT_DURATION_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(NetworkConstants.TIMEOUT_DURATION_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    @Authenticator
    @Singleton
    @Provides
    fun provideAuthenticatorRetrofit(
        @Authenticator authenticatorOkHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NetworkConstants.BASE_URL)
            .client(authenticatorOkHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Authenticator
    @Singleton
    @Provides
    fun provideAuthenticatorAuthApi(@Authenticator authenticatorRetrofit: Retrofit): AuthApi {
        return authenticatorRetrofit.create(AuthApi::class.java)
    }

    @Singleton
    @Provides
    fun provideTokenAuthenticator(
        tokenDataStoreService: TokenDataStoreService,
        @Authenticator authRepository: AuthRepository,
    ): TokenAuthenticator {
        return TokenAuthenticator(tokenDataStoreService, authRepository)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator,
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor)
            .authenticator(tokenAuthenticator)
            .connectTimeout(NetworkConstants.TIMEOUT_DURATION_SECONDS, TimeUnit.SECONDS)
            .readTimeout(NetworkConstants.TIMEOUT_DURATION_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(NetworkConstants.TIMEOUT_DURATION_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NetworkConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }
}
