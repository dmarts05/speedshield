package com.dmarts05.speedshield.data.network

import com.dmarts05.speedshield.data.service.TokenDataStoreService
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenDataStoreService: TokenDataStoreService,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        // Retrieve the token synchronously
        val token: String? = runBlocking {
            try {
                tokenDataStoreService.getToken()
            } catch (e: IllegalArgumentException) {
                null
            }
        }

        // Add the Authorization header if the token is available
        val authenticatedRequest = token?.let {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $it")
                .build()
        } ?: request

        return chain.proceed(authenticatedRequest)
    }
}
