package com.dmarts05.speedshield.data.network


import com.dmarts05.speedshield.data.model.RefreshRequestDto
import com.dmarts05.speedshield.data.repository.AuthRepository
import com.dmarts05.speedshield.data.service.TokenDataStoreService
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenDataStoreService: TokenDataStoreService,
    private val authRepository: AuthRepository,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val originalRequest = response.request

        // Do not retry more than once
        if (responseCount(response) >= 2) {
            return null
        }

        return runBlocking {
            try {
                var token = tokenDataStoreService.getToken()
                var refreshToken = tokenDataStoreService.getRefreshToken()

                // Attempt to refresh the token
                val refreshRequestDto = RefreshRequestDto(
                    token = token,
                    refreshToken = refreshToken,
                )
                authRepository.refresh(refreshRequestDto).collect {
                    it.data?.let { tokenResponseDto ->
                        // Store new tokens and update the token variables
                        tokenDataStoreService.storeTokens(
                            token = tokenResponseDto.token,
                            refreshToken = tokenResponseDto.refreshToken,
                        )
                        token = tokenResponseDto.token
                        refreshToken = tokenResponseDto.refreshToken
                    }
                }

                // Retry the original request with the new token
                originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
            } catch (e: Exception) {
                // If refreshing the token fails, return null to indicate no retry
                null
            }
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var priorResponse = response.priorResponse
        while (priorResponse != null) {
            count++
            priorResponse = priorResponse.priorResponse
        }
        return count
    }
}