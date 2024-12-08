package com.dmarts05.speedshield.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation


@Composable
fun ShowHidePasswordField(
    password: String,
    extraKeyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    extraKeyboardActions: KeyboardActions = KeyboardActions.Default,
    onTextFieldChanged: (String) -> Unit,
) {
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = { onTextFieldChanged(it) },
        placeholder = { Text(text = "Password") },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (isPasswordVisible) VisualTransformation.None
        else PasswordVisualTransformation(),
        trailingIcon = {
            SwitchPasswordVisibilityBtn(isPasswordVisible) {
                isPasswordVisible = !isPasswordVisible
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ).merge(extraKeyboardOptions),
        keyboardActions = extraKeyboardActions,
        singleLine = true,
        maxLines = 1,
    )
}

@Composable
private fun SwitchPasswordVisibilityBtn(
    isPasswordVisible: Boolean,
    onPasswordVisibilityChanged: () -> Unit,
) {
    IconButton(onClick = { onPasswordVisibilityChanged() }) {
        Icon(
            imageVector = if (isPasswordVisible) Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff,
            contentDescription = "password_visibility"
        )
    }
}
