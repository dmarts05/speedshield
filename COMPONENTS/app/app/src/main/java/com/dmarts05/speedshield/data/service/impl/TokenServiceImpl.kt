package com.dmarts05.speedshield.data.service.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dmarts05.speedshield.data.service.TokenService
import jakarta.inject.Inject
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class TokenServiceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : TokenService {
    companion object {
        val TOKEN = stringPreferencesKey("token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    override suspend fun isAuthenticated(): Boolean {
        return dataStore.data.map {
            it.contains(TOKEN) && it.contains(REFRESH_TOKEN)
        }.firstOrNull() ?: false
    }

    override suspend fun storeTokens(token: String, refreshToken: String) {
        dataStore.edit {
            it[TOKEN] = token
            it[REFRESH_TOKEN] = refreshToken
        }
    }

    override suspend fun getToken(): String {
        return dataStore.data
            .map { it[TOKEN] }
            .firstOrNull()
            ?: throw IllegalArgumentException("no token stored")
    }

    override suspend fun getRefreshToken(): String {
        return dataStore.data
            .map { it[REFRESH_TOKEN] }
            .firstOrNull()
            ?: throw IllegalArgumentException("no refresh token stored")
    }

    override suspend fun removeTokens() {
        dataStore.edit {
            it.remove(TOKEN)
            it.remove(REFRESH_TOKEN)
        }
    }

}