package dev.guillemdiaz.freenowdemo.ui.navigation

/**
 * Defines all possible navigation destinations in the app.
 * Each object holds the route string used by the NavController.
 */
sealed class NavDestination(val route: String) {
    data object Home : NavDestination("home")
    data object Trips : NavDestination("trips")
    data object Wallet : NavDestination("wallet")
    data object Account : NavDestination("account")
    data object Destination : NavDestination("destination")
}
