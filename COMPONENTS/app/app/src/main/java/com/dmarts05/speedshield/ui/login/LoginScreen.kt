package com.dmarts05.speedshield.ui.login

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
import com.dmarts05.speedshield.ui.components.EmailField
import com.dmarts05.speedshield.ui.components.Logo
import com.dmarts05.speedshield.ui.components.ShowHidePasswordField
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel<LoginViewModel>()
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
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    if (isLoading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    } else {
        Column(modifier = modifier) {
            Logo(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                logoSize = 120.dp,
                textStyle = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.padding(16.dp))
            EmailField(email = email,
                extraKeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                extraKeyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                onTextFieldChanged = { viewModel.onLoginChanged(it, password) }
            )
            Spacer(modifier = Modifier.padding(8.dp))
            ShowHidePasswordField(
                password = password,
                extraKeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                extraKeyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        coroutineScope.launch {
                            viewModel.onLoginBtnPressed()
                        }
                    }
                ),
                onTextFieldChanged = { viewModel.onLoginChanged(email, it) }
            )
            Spacer(modifier = Modifier.padding(8.dp))
            LoginBtn(isLoginEnabled) {
                coroutineScope.launch {
                    viewModel.onLoginBtnPressed()
                }
            }
        }
    }
}

@Composable
fun LoginBtn(loginEnable: Boolean, onLoginSelected: () -> Unit) {
    Button(
        onClick = { onLoginSelected() },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        enabled = loginEnable
    ) {
        Text(text = "Log in")
    }
}
