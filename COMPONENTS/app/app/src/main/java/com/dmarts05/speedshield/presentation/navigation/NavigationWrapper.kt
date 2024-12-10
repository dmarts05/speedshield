package com.dmarts05.speedshield.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dmarts05.speedshield.data.service.TokenDataStoreService
import com.dmarts05.speedshield.presentation.ui.screens.HomeScreen
import com.dmarts05.speedshield.presentation.ui.screens.LandingPageScreen
import com.dmarts05.speedshield.presentation.ui.screens.LoginScreen
import com.dmarts05.speedshield.presentation.ui.screens.RegisterScreen
import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking

class NavigationWrapper @Inject constructor(private val tokenDataStoreService: TokenDataStoreService) {
    @Composable
    fun Component() {
        val isAuthenticated = runBlocking { tokenDataStoreService.isAuthenticated() }
        val navController = rememberNavController()
        LandingScaffold { baseModifier ->
            NavHost(
                navController = navController,
                startDestination = if (!isAuthenticated) LandingPage else Home
            ) {
                composable<LandingPage> {
                    LandingPageScreen(
                        modifier = baseModifier,
                        navigateToLogin = { navigateToLogin(navController) },
                        navigateToRegister = { navigateToRegister(navController) }
                    )
                }

                composable<Login> {
                    LoginScreen(
                        modifier = baseModifier,
                        navigateToHome = { navigateToHome(navController) },
                        navigateToRegister = { navigateToRegister(navController) }
                    )
                }

                composable<Register> {
                    RegisterScreen(
                        modifier = baseModifier,
                        navigateToHome = { navigateToHome(navController) },
                        navigateToLogin = { navigateToLogin(navController) }
                    )
                }

                composable<Home> {
                    HomeScreen()
                }
            }
        }
    }

    @Composable
    private fun LandingScaffold(content: @Composable (Modifier) -> Unit) {
        return Scaffold(
            content = { innerPadding ->
                val modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp, 16.dp, 16.dp, innerPadding.calculateBottomPadding())
                content(modifier)
            }
        )
    }

    private fun navigateToLogin(navController: NavController) {
        navController.navigate(Login) {
            popUpTo(LandingPage) { inclusive = false }
        }
    }

    private fun navigateToRegister(navController: NavController) {
        navController.navigate(Register) {
            popUpTo(LandingPage) { inclusive = false }
        }
    }

    private fun navigateToHome(navController: NavController) {
        navController.navigate(Home) {
            // Clear the back stack to prevent the user from going back to the login or register screen
            popUpTo(0) { inclusive = false }
        }
    }
}

