package com.dmarts05.speedshield.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dmarts05.speedshield.presentation.ui.components.Map

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Map(modifier = Modifier.fillMaxSize())
    }
}
