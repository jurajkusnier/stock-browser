package com.juraj.stocksbrowser.navigation

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.juraj.stocksbrowser.ui.home.HomeScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavDestinations.Home.url) {
        composable(route = NavDestinations.Home.url) {
            HomeScreen(hiltViewModel(), navController)
        }
        composable(route = NavDestinations.Details.url) {
            Text("Welcome to detail of ${it.arguments}")
        }
    }
}

sealed class NavDestinations(val url: String) {
    object Home : NavDestinations("home")
    object Details : NavDestinations("details?symbol={symbol}") {
        fun uri(symbol: String) = url.replace("{symbol}", symbol)
    }
}