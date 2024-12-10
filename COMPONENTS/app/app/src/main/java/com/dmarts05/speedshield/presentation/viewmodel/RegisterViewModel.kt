package com.dmarts05.speedshield.presentation.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmarts05.speedshield.data.model.RegisterRequestDto
import com.dmarts05.speedshield.data.model.TokenResponseDto
import com.dmarts05.speedshield.data.network.NetworkResult
import com.dmarts05.speedshield.data.repository.AuthRepository
import com.dmarts05.speedshield.data.service.TokenDataStoreService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenDataStoreService: TokenDataStoreService,
) :
    ViewModel() {
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _isRegisterEnabled = MutableLiveData<Boolean>()
    val isRegisterEnabled: LiveData<Boolean> = _isRegisterEnabled

    private val _registerResponse =
        MutableLiveData<NetworkResult<TokenResponseDto>>()
    val registerResponse: LiveData<NetworkResult<TokenResponseDto>> = _registerResponse

    fun onRegisterChanged(username: String, email: String, password: String) {
        _username.value = username
        _email.value = email
        _password.value = password
        _isRegisterEnabled.value = isUsernameValid(username) &&
                isEmailValid(email) &&
                isPasswordValid(password) &&
                _registerResponse.value !is NetworkResult.Loading
    }

    private fun isUsernameValid(username: String): Boolean = username.isNotEmpty()

    private fun isPasswordValid(password: String): Boolean = password.length >= 8

    private fun isEmailValid(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun register() = viewModelScope.launch {
        val registerRequestDto =
            RegisterRequestDto(
                email = email.value!!,
                username = username.value!!,
                password = password.value!!,
            )
        authRepository.register(registerRequestDto).collect {
            _registerResponse.value = it
            it.data?.let { tokenResponseDto ->
                tokenDataStoreService.storeTokens(
                    tokenResponseDto.token,
                    tokenResponseDto.refreshToken
                )
            }
        }
    }
}
