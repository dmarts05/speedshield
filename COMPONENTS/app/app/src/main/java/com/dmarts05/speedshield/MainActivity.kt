package com.dmarts05.speedshield

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dmarts05.speedshield.presentation.navigation.NavigationWrapper
import com.dmarts05.speedshield.presentation.ui.theme.SpeedshieldTheme
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigationWrapper: NavigationWrapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeedshieldTheme {
                navigationWrapper.Component()
            }
        }
    }
}
