package com.dmarts05.speedshield

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import com.dmarts05.speedshield.data.local.tokenDataStore
import com.dmarts05.speedshield.data.service.impl.TokenServiceImpl
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
                val navigationWrapper =
                    NavigationWrapper(TokenServiceImpl(LocalContext.current.tokenDataStore))
                navigationWrapper.Component()
            }
        }
    }
}
