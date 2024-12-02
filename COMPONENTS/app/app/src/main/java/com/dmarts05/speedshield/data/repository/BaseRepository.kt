package com.dmarts05.speedshield.data.repository

import com.dmarts05.speedshield.data.network.NetworkResult
import retrofit2.Response

abstract class BaseRepository {
    protected suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body)
                }
            }
            val errorMessage =
                response.errorBody()?.string()?.split("\"message\":\"")[1]?.split("\"")[0]
            return NetworkResult.Error("API call failed (${response.code()} ${response.message()}): $errorMessage")
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: e.toString())
        }
    }
}