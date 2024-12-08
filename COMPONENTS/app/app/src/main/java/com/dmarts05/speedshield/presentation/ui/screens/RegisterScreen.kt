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
import com.dmarts05.speedshield.presentation.ui.components.GenericTextField
import com.dmarts05.speedshield.presentation.ui.components.Logo
import com.dmarts05.speedshield.presentation.ui.components.ShowHidePasswordField
import com.dmarts05.speedshield.presentation.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    navigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel<RegisterViewModel>(),
) {
    Box(
        modifier = modifier
    ) {
        Register(Modifier.align(Alignment.Center), viewModel, navigateToHome, navigateToLogin)
    }
}

@Composable
private fun Register(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel,
    navigateToHome: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    val username: String by viewModel.username.observeAsState(initial = "")
    val email: String by viewModel.email.observeAsState(initial = "")
    val password: String by viewModel.password.observeAsState(initial = "")
    val isRegisterEnabled: Boolean by viewModel.isRegisterEnabled.observeAsState(initial = false)
    val registerResponse by viewModel.registerResponse.observeAsState()

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
            // Username Field
            GenericTextField(
                value = username,
                placeholderText = "Username",
                extraKeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                extraKeyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(
                        FocusDirection.Down
                    )
                }),
                onTextFieldChanged = { viewModel.onRegisterChanged(it, email, password) }
            )
            EmailField(
                email = email,
                extraKeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                extraKeyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                onTextFieldChanged = { viewModel.onRegisterChanged(username, it, password) }
            )
            ShowHidePasswordField(
                password = password,
                extraKeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                extraKeyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        if (isRegisterEnabled) {
                            viewModel.register()
                        }
                    }
                ),
                onTextFieldChanged = { viewModel.onRegisterChanged(username, email, it) }
            )
        }
        RegisterBtn(
            isRegisterEnabled = isRegisterEnabled,
            onRegisterSelected = { viewModel.register() })
        TextButton(
            onClick = navigateToLogin,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Already have an account? Log in here.")
        }

        // Handle register response
        registerResponse?.let { response ->
            when (response) {
                is NetworkResult.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                is NetworkResult.Success -> {
                    Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT).show()
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
private fun RegisterBtn(isRegisterEnabled: Boolean, onRegisterSelected: () -> Unit) {
    Button(
        onClick = { onRegisterSelected() },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        enabled = isRegisterEnabled
    ) {
        Text(text = "Register")
    }
}
