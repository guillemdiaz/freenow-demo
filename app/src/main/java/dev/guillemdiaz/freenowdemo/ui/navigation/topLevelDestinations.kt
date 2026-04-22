@file:Suppress("ktlint:standard:filename")

package dev.guillemdiaz.freenowdemo.ui.navigation

import dev.guillemdiaz.freenowdemo.R
import dev.guillemdiaz.freenowdemo.core.designsystem.icon.FreenowIcons

/**
 * Ordered list of destinations rendered in the bottom navigation bar.
 * The order here determines the order they appear left to right in the UI.
 */
val topLevelDestinations = listOf(
    TopLevelDestination(
        destination = NavDestination.Home,
        icon = FreenowIcons.Home,
        selectedIcon = FreenowIcons.SelectedHome,
        label = R.string.nav_home
    ),
    TopLevelDestination(
        destination = NavDestination.Trips,
        icon = FreenowIcons.Trips,
        selectedIcon = FreenowIcons.SelectedTrips,
        label = R.string.nav_trips
    ),
    TopLevelDestination(
        destination = NavDestination.Wallet,
        icon = FreenowIcons.Wallet,
        selectedIcon = FreenowIcons.SelectedWallet,
        label = R.string.nav_wallet
    ),
    TopLevelDestination(
        destination = NavDestination.Account,
        icon = FreenowIcons.Account,
        selectedIcon = FreenowIcons.SelectedAccount,
        label = R.string.nav_account
    )
)
