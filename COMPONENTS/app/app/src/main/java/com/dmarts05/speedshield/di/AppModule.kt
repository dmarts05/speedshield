package com.dmarts05.speedshield.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dmarts05.speedshield.data.local.tokenDataStore
import com.dmarts05.speedshield.data.network.AuthApi
import com.dmarts05.speedshield.data.network.AuthInterceptor
import com.dmarts05.speedshield.data.repository.AuthRepository
import com.dmarts05.speedshield.data.repository.impl.AuthRepositoryImpl
import com.dmarts05.speedshield.data.service.TokenService
import com.dmarts05.speedshield.data.service.impl.TokenServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideTokensDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.tokenDataStore
    }

    @Singleton
    @Provides
    fun provideTokenService(dataStore: DataStore<Preferences>): TokenService {
        return TokenServiceImpl(dataStore)
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(tokenService: TokenService): AuthInterceptor {
        return AuthInterceptor(tokenService)
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Singleton
    @Provides
    fun provideAuthRepository(
        authApi: AuthApi,
    ): AuthRepository {
        return AuthRepositoryImpl(authApi)
    }
}
