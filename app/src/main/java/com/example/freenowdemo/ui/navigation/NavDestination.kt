package com.example.freenowdemo.ui.navigation

sealed class NavDestination(val route: String) {
    object Home : NavDestination("home")
    object Trips : NavDestination("trips")
    object Wallet : NavDestination("wallet")
    object Account : NavDestination("account")
}
