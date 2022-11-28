package com.juraj.stocksbrowser.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.juraj.stocksbrowser.ui.detail.screen.DetailScreen
import com.juraj.stocksbrowser.ui.home.screen.HomeScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavDestinations.Home.url) {
        composable(route = NavDestinations.Home.url) {
            HomeScreen(hiltViewModel(), navController)
        }
        composable(route = NavDestinations.Details.url) {
            DetailScreen(hiltViewModel(), navController)
        }
    }
}

sealed class NavDestinations(val url: String) {
    object Home : NavDestinations("home")
    object Details : NavDestinations("details?symbol={symbol}") {
        fun uri(symbol: String) = url.replace("{symbol}", symbol)
        fun getSymbol(savedStateHandle: SavedStateHandle):String? = savedStateHandle["symbol"]
    }
}