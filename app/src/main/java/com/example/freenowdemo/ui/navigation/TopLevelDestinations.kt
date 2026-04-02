package com.example.freenowdemo.ui.navigation

import com.example.freenowdemo.R
import com.example.freenowdemo.core.designsystem.icon.FreenowIcons

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
