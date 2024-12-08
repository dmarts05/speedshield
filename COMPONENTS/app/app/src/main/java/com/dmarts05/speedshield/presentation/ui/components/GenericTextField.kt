package com.dmarts05.speedshield.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun GenericTextField(
    value: String,
    placeholderText: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    extraKeyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    extraKeyboardActions: KeyboardActions = KeyboardActions.Default,
    onTextFieldChanged: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onTextFieldChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = placeholderText) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ).merge(extraKeyboardOptions),
        keyboardActions = extraKeyboardActions,
        singleLine = true,
        maxLines = 1,
    )
}
