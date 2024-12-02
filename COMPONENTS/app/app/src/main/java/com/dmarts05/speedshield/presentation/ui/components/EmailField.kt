package com.dmarts05.speedshield.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun EmailField(
    email: String,
    extraKeyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    extraKeyboardActions: KeyboardActions = KeyboardActions.Default,
    onTextFieldChanged: (String) -> Unit
) {
    TextField(
        value = email, onValueChange = { onTextFieldChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Email") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email
        ).merge(extraKeyboardOptions),
        keyboardActions = extraKeyboardActions,
        singleLine = true,
        maxLines = 1,
    )
}
