package com.dmarts05.speedshield.presentation.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmarts05.speedshield.data.model.LoginRequestDto
import com.dmarts05.speedshield.data.model.TokenResponseDto
import com.dmarts05.speedshield.data.network.NetworkResult
import com.dmarts05.speedshield.data.repository.AuthRepository
import com.dmarts05.speedshield.data.service.TokenService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenService: TokenService,
) : ViewModel() {
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _isLoginEnabled = MutableLiveData<Boolean>()
    val isLoginEnabled: LiveData<Boolean> = _isLoginEnabled

    private val _loginResponse = MutableLiveData<NetworkResult<TokenResponseDto>>()
    val loginResponse: LiveData<NetworkResult<TokenResponseDto>> = _loginResponse

    fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _isLoginEnabled.value =
            isEmailValid(email) && isPasswordValid(password) && _loginResponse.value !is NetworkResult.Loading
    }

    private fun isPasswordValid(password: String): Boolean = password.length >= 8

    private fun isEmailValid(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun login() = viewModelScope.launch {
        val loginRequestDto = LoginRequestDto(
            email = email.value!!,
            password = password.value!!,
        )
        authRepository.login(loginRequestDto).collect {
            _loginResponse.value = it
            it.data?.let { tokenResponseDto ->
                tokenService.storeTokens(tokenResponseDto.token, tokenResponseDto.refreshToken)
            }
        }
    }
}