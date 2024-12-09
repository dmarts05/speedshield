package com.dmarts05.speedshield.data.repository

import com.dmarts05.speedshield.data.model.LoginRequestDto
import com.dmarts05.speedshield.data.model.LogoutRequestDto
import com.dmarts05.speedshield.data.model.RefreshRequestDto
import com.dmarts05.speedshield.data.model.RegisterRequestDto
import com.dmarts05.speedshield.data.model.TokenResponseDto
import com.dmarts05.speedshield.data.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun register(registerRequestDto: RegisterRequestDto): Flow<NetworkResult<TokenResponseDto>>

    fun login(loginRequestDto: LoginRequestDto): Flow<NetworkResult<TokenResponseDto>>

    fun refresh(refreshRequestDto: RefreshRequestDto): Flow<NetworkResult<TokenResponseDto>>

    fun logout(refreshRequestDto: LogoutRequestDto): Flow<NetworkResult<Unit>>
}