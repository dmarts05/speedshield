package com.dmarts05.speedshield.data.service

interface TokenService {
    suspend fun isAuthenticated(): Boolean
    suspend fun storeTokens(token: String, refreshToken: String)
    suspend fun getToken(): String
    suspend fun getRefreshToken(): String
    suspend fun removeTokens()
}