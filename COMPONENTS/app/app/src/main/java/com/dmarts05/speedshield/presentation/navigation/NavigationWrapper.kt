package com.dmarts05.speedshield.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dmarts05.speedshield.data.service.TokenDataStoreService
import com.dmarts05.speedshield.presentation.ui.screens.*
import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking


class NavigationWrapper @Inject constructor(private val tokenDataStoreService: TokenDataStoreService) {
    @Composable
    fun Component() {
        val isAuthenticated = runBlocking { tokenDataStoreService.isAuthenticated() }
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = if (!isAuthenticated) LandingPage else Home
        ) {
            // Landing, Login, and Register Screens inside LandingScaffold
            composable<LandingPage> {
                LandingScaffold { baseModifier ->
                    LandingPageScreen(
                        modifier = baseModifier,
                        navigateToLogin = { navigateToLogin(navController) },
                        navigateToRegister = { navigateToRegister(navController) }
                    )
                }
            }

            composable<Login> {
                LandingScaffold { baseModifier ->
                    LoginScreen(
                        modifier = baseModifier,
                        navigateToHome = { navigateToHome(navController) },
                        navigateToRegister = { navigateToRegister(navController) }
                    )
                }
            }

            composable<Register> {
                LandingScaffold { baseModifier ->
                    RegisterScreen(
                        modifier = baseModifier,
                        navigateToHome = { navigateToHome(navController) },
                        navigateToLogin = { navigateToLogin(navController) }
                    )
                }
            }

            // AppScaffold for other screens with bottom navigation bar
            composable<Home> {
                NavScaffold(navController) { baseModifier ->
                    HomeScreen(baseModifier)
                }
            }

            composable<Settings> {
                AppScaffold(navController) { baseModifier ->
                    SettingsScreen(baseModifier)
                }
            }

            composable<Donate> {
                AppScaffold(navController) { baseModifier ->
                    DonateScreen(baseModifier)
                }
            }
        }
    }

    @Composable
    private fun LandingScaffold(content: @Composable (Modifier) -> Unit) {
        Scaffold(
            content = { innerPadding ->
                val modifier = remember {
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp, 16.dp, 16.dp, innerPadding.calculateBottomPadding())
                }
                content(modifier)
            }
        )
    }

    @Composable
    private fun AppScaffold(
        navController: NavController,
        content: @Composable (Modifier) -> Unit,
    ) {
        Scaffold(
            bottomBar = {
                BottomBar(navController)
            },
            content = { innerPadding ->
                val modifier = remember {
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp, 16.dp, 16.dp, innerPadding.calculateBottomPadding())
                }
                content(modifier)
            }
        )
    }

    @Composable
    private fun NavScaffold(
        navController: NavController,
        content: @Composable (Modifier) -> Unit,
    ) {
        Scaffold(
            bottomBar = {
                BottomBar(navController)
            },
            content = { innerPadding ->
                val modifier = remember {
                    Modifier
                        .fillMaxSize()
                        .padding(0.dp, innerPadding.calculateTopPadding(), 0.dp, 0.dp)
                }
                Box(modifier) {
                    content(modifier)
                    FloatingActionButton(
                        onClick = {
                            // TODO
                        },
                        shape = CircleShape,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(
                                0.dp,
                                0.dp,
                                0.dp,
                                innerPadding.calculateBottomPadding() + 16.dp
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "Start Speedshield",
                        )
                    }
                }
            }
        )
    }

    @Composable
    private fun BottomBar(navController: NavController) {
        NavigationBar {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            APP_DESTINATION_ITEMS.forEach { destination ->
                val destinationRoute = destination.route::class.qualifiedName
                val selected =
                    currentDestination?.hierarchy?.any { it.route == destinationRoute } == true

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = if (selected) destination.iconSelected else destination.iconUnselected,
                            contentDescription = destination.name
                        )
                    },
                    label = { Text(destination.name) },
                    selected = selected,
                    onClick = {
                        navController.navigate(destination.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
        }
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
            popUpTo(0) { inclusive = false }
        }
    }
}
