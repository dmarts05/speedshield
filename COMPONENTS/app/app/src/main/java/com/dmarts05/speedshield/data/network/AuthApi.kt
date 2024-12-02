package com.dmarts05.speedshield.data.network

import com.dmarts05.speedshield.data.model.LoginRequestDto
import com.dmarts05.speedshield.data.model.LogoutRequestDto
import com.dmarts05.speedshield.data.model.RefreshRequestDto
import com.dmarts05.speedshield.data.model.RegisterRequestDto
import com.dmarts05.speedshield.data.model.TokenResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/v1/auth/register")
    suspend fun register(@Body registerRequest: RegisterRequestDto): Response<TokenResponseDto>

    @POST("/api/v1/auth/login")
    suspend fun login(@Body loginRequest: LoginRequestDto): Response<TokenResponseDto>

    @POST("/api/v1/auth/refresh")
    suspend fun refresh(@Body refreshRequest: RefreshRequestDto): Response<TokenResponseDto>

    @POST("/api/v1/auth/logout")
    suspend fun logout(@Body logoutRequest: LogoutRequestDto): Response<Unit>
}
