package dev.guillemdiaz.freenowdemo.ui.navigation

import kotlinx.serialization.Serializable

/**
 * Defines all possible navigation destinations in the app.
 * Each object holds the route string used by the NavController.
 */
sealed interface NavDestination {
    @Serializable
    data object Home : NavDestination

    @Serializable
    data object Trips : NavDestination

    @Serializable
    data object Wallet : NavDestination

    @Serializable
    data object Account : NavDestination

    @Serializable
    data object Destination : NavDestination
}
