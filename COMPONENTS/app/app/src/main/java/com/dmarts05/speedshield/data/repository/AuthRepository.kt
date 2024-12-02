package com.dmarts05.speedshield.data.repository

import com.dmarts05.speedshield.data.model.LoginRequestDto
import com.dmarts05.speedshield.data.model.LogoutRequestDto
import com.dmarts05.speedshield.data.model.RefreshRequestDto
import com.dmarts05.speedshield.data.model.RegisterRequestDto
import com.dmarts05.speedshield.data.model.TokenResponseDto
import com.dmarts05.speedshield.data.network.AuthApi
import com.dmarts05.speedshield.data.network.NetworkResult
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ViewModelScoped
class AuthRepository @Inject constructor(private val authApi: AuthApi) : BaseRepository() {
    fun register(registerRequestDto: RegisterRequestDto): Flow<NetworkResult<TokenResponseDto>> {
        return flow {
            emit(safeApiCall { authApi.register(registerRequestDto) })
        }.flowOn(Dispatchers.IO)
    }

    fun login(loginRequestDto: LoginRequestDto): Flow<NetworkResult<TokenResponseDto>> {
        return flow {
            emit(safeApiCall { authApi.login(loginRequestDto) })
        }.flowOn(Dispatchers.IO)
    }

    fun refresh(refreshRequestDto: RefreshRequestDto): Flow<NetworkResult<TokenResponseDto>> {
        return flow {
            emit(safeApiCall { authApi.refresh(refreshRequestDto) })
        }.flowOn(Dispatchers.IO)
    }

    fun logout(refreshRequestDto: LogoutRequestDto): Flow<NetworkResult<Unit>> {
        return flow {
            emit(safeApiCall { authApi.logout(refreshRequestDto) })
        }.flowOn(Dispatchers.IO)
    }
}
