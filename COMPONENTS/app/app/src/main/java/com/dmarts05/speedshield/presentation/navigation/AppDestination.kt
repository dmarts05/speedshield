package com.dmarts05.speedshield.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.ui.graphics.vector.ImageVector

data class AppDestination<T : Any>(
    val name: String,
    val route: T,
    val iconSelected: ImageVector,
    val iconUnselected: ImageVector,
)

val APP_DESTINATION_ITEMS = listOf(
    AppDestination(
        "Donate",
        Donate,
        Icons.Filled.VolunteerActivism,
        Icons.Outlined.VolunteerActivism
    ),
    AppDestination("Home", Home, Icons.Filled.Home, Icons.Outlined.Home),
    AppDestination("Settings", Settings, Icons.Filled.Settings, Icons.Outlined.Settings),
)
