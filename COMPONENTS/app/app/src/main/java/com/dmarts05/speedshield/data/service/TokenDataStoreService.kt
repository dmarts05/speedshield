package com.dmarts05.speedshield.data.service

interface TokenDataStoreService {
    suspend fun isAuthenticated(): Boolean
    suspend fun storeTokens(token: String, refreshToken: String)
    suspend fun getToken(): String
    suspend fun getRefreshToken(): String
    suspend fun removeTokens()
}