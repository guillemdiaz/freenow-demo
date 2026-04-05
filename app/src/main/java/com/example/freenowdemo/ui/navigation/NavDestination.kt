package com.example.freenowdemo.ui.navigation

/**
 * Defines all possible navigation destinations in the app.
 * Each object holds the route string used by the NavController.
 */
sealed class NavDestination(val route: String) {
    object Home : NavDestination("home")
    object Trips : NavDestination("trips")
    object Wallet : NavDestination("wallet")
    object Account : NavDestination("account")
}
