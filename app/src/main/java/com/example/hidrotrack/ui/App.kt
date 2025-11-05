package com.example.hidrotrack.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hidrotrack.ui.screens.HistoryScreen
import com.example.hidrotrack.ui.screens.HomeScreen
import com.example.hidrotrack.ui.screens.SettingsScreen

object Destinations {
    const val HOME = "home"
    const val SETTINGS = "settings"
    const val HISTORY = "history"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Destinations.HOME) {
        composable(Destinations.HOME) { HomeScreen(navController) }
        composable(Destinations.SETTINGS) { SettingsScreen(navController) }
        composable(Destinations.HISTORY) { HistoryScreen(navController) }
    }
}
