package com.dmarts05.speedshield.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dmarts05.speedshield.data.network.NetworkResult
import com.dmarts05.speedshield.presentation.ui.components.EmailField
import com.dmarts05.speedshield.presentation.ui.components.Logo
import com.dmarts05.speedshield.presentation.ui.components.ShowHidePasswordField
import com.dmarts05.speedshield.presentation.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel<LoginViewModel>(),
) {
    Box(
        modifier = modifier
    ) {
        Login(Modifier.align(Alignment.Center), viewModel)
    }
}

@Composable
fun Login(modifier: Modifier = Modifier, viewModel: LoginViewModel) {
    val email: String by viewModel.email.observeAsState(initial = "")
    val password: String by viewModel.password.observeAsState(initial = "")
    val isLoginEnabled: Boolean by viewModel.isLoginEnabled.observeAsState(initial = false)
    val loginResponse by viewModel.loginResponse.observeAsState()

    val focusManager = LocalFocusManager.current

    Column(modifier = modifier.padding(16.dp)) {
        Logo(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            logoSize = 120.dp,
            textStyle = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        EmailField(
            email = email,
            extraKeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            extraKeyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            onTextFieldChanged = { viewModel.onLoginChanged(it, password) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        ShowHidePasswordField(
            password = password,
            extraKeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            extraKeyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    if (isLoginEnabled) {
                        viewModel.login()
                    }
                }
            ),
            onTextFieldChanged = { viewModel.onLoginChanged(email, it) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        LoginBtn(isLoginEnabled = isLoginEnabled, onLoginSelected = { viewModel.login() })

        // Handle login response
        loginResponse?.let { response ->
            Spacer(modifier = Modifier.height(16.dp))
            when (response) {
                is NetworkResult.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                is NetworkResult.Success -> {
                    Text(
                        text = "Login Successful!",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                is NetworkResult.Error -> {
                    Text(
                        text = "Error: ${response.message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
fun LoginBtn(isLoginEnabled: Boolean, onLoginSelected: () -> Unit) {
    Button(
        onClick = { onLoginSelected() },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        enabled = isLoginEnabled
    ) {
        Text(text = "Log in")
    }
}
