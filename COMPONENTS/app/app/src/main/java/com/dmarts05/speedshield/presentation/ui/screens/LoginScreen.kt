package com.dmarts05.speedshield.presentation.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
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
    navigateToHome: () -> Unit,
    navigateToRegister: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel<LoginViewModel>(),
) {
    Box(
        modifier = modifier
    ) {
        Login(Modifier.align(Alignment.Center), viewModel, navigateToHome, navigateToRegister)
    }
}

@Composable
private fun Login(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel,
    navigateToHome: () -> Unit,
    navigateToRegister: () -> Unit,
) {
    val email: String by viewModel.email.observeAsState(initial = "")
    val password: String by viewModel.password.observeAsState(initial = "")
    val isLoginEnabled: Boolean by viewModel.isLoginEnabled.observeAsState(initial = false)
    val loginResponse by viewModel.loginResponse.observeAsState()

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Logo(
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            EmailField(
                email = email,
                extraKeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                extraKeyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                onTextFieldChanged = { viewModel.onLoginChanged(it, password) }
            )
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
        }
        LoginBtn(isLoginEnabled = isLoginEnabled, onLoginSelected = { viewModel.login() })
        TextButton(
            onClick = navigateToRegister,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Don't have an account? Register here.")
        }

        // Handle login response
        loginResponse?.let { response ->
            when (response) {
                is NetworkResult.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                is NetworkResult.Success -> {
                    Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                    navigateToHome()
                }

                is NetworkResult.Error -> {
                    Toast.makeText(context, "Error: ${response.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

@Composable
private fun LoginBtn(isLoginEnabled: Boolean, onLoginSelected: () -> Unit) {
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
