package com.dmarts05.speedshield.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dmarts05.speedshield.presentation.ui.components.Logo

@Composable
fun LandingPageScreen(
    modifier: Modifier = Modifier,
    navigateToLogin: () -> Unit,
    navigateToRegister: () -> Unit,
) {
    Box(
        modifier = modifier
    ) {
        LandingPage(Modifier.align(Alignment.Center), navigateToLogin, navigateToRegister)
    }
}

@Composable
private fun LandingPage(
    modifier: Modifier = Modifier,
    navigateToLogin: () -> Unit,
    navigateToRegister: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Logo(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            showText = false
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Welcome to Speedshield!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // Login Button
            Button(
                onClick = { navigateToLogin() },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text(text = "Log in")
            }
            // Register Button
            Button(
                onClick = { navigateToRegister() },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text(text = "Register")
            }
        }
    }
}
