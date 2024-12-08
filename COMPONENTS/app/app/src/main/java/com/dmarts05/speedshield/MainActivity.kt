package com.dmarts05.speedshield

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dmarts05.speedshield.presentation.navigation.NavigationWrapper
import com.dmarts05.speedshield.presentation.ui.theme.SpeedshieldTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeedshieldTheme {
                NavigationWrapper()
            }
        }
    }
}
