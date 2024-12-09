package com.dmarts05.speedshield.data.repository.impl

import com.dmarts05.speedshield.data.model.LoginRequestDto
import com.dmarts05.speedshield.data.model.LogoutRequestDto
import com.dmarts05.speedshield.data.model.RefreshRequestDto
import com.dmarts05.speedshield.data.model.RegisterRequestDto
import com.dmarts05.speedshield.data.model.TokenResponseDto
import com.dmarts05.speedshield.data.network.AuthApi
import com.dmarts05.speedshield.data.network.NetworkResult
import com.dmarts05.speedshield.data.repository.AuthRepository
import com.dmarts05.speedshield.data.repository.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authApi: AuthApi) : BaseRepository(),
    AuthRepository {
    override fun register(registerRequestDto: RegisterRequestDto): Flow<NetworkResult<TokenResponseDto>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { authApi.register(registerRequestDto) })
        }.flowOn(Dispatchers.IO)
    }

    override fun login(loginRequestDto: LoginRequestDto): Flow<NetworkResult<TokenResponseDto>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { authApi.login(loginRequestDto) })
        }.flowOn(Dispatchers.IO)
    }

    override fun refresh(refreshRequestDto: RefreshRequestDto): Flow<NetworkResult<TokenResponseDto>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { authApi.refresh(refreshRequestDto) })
        }.flowOn(Dispatchers.IO)
    }

    override fun logout(refreshRequestDto: LogoutRequestDto): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { authApi.logout(refreshRequestDto) })
        }.flowOn(Dispatchers.IO)
    }
}
