package dev.guillemdiaz.freenowdemo.ui.navigation

import kotlinx.serialization.Serializable

/**
 * Defines all possible navigation destinations in the app.
 * Each object holds the route string used by the NavController.
 */
sealed interface NavDestination {
    @kotlinx.serialization.Serializable
    data object Home : NavDestination

    @kotlinx.serialization.Serializable
    data object Trips : NavDestination

    @kotlinx.serialization.Serializable
    data object Wallet : NavDestination

    @kotlinx.serialization.Serializable
    data object Account : NavDestination

    @Serializable
    data object Destination : NavDestination
}
