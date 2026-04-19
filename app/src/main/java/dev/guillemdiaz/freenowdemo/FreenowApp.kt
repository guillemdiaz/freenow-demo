package dev.guillemdiaz.freenowdemo

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.guillemdiaz.freenowdemo.core.designsystem.component.FreenowNavigationBar
import dev.guillemdiaz.freenowdemo.core.designsystem.component.FreenowNavigationBarItem
import dev.guillemdiaz.freenowdemo.core.network.NetworkMonitor
import dev.guillemdiaz.freenowdemo.feature.booking.BookingRoute
import dev.guillemdiaz.freenowdemo.feature.destination.DestinationRoute
import dev.guillemdiaz.freenowdemo.ui.navigation.NavDestination
import dev.guillemdiaz.freenowdemo.ui.navigation.topLevelDestinations

/**
 * Root composable of the app. Sets up the navigation controller, bottom navigation bar,
 * and the NavHost with all the top-level destinations.
 */
@Composable
fun FreenowApp(networkMonitor: NetworkMonitor) {
    val appState = rememberFreenowAppState(networkMonitor = networkMonitor)
    val isOffline by appState.isOffline.collectAsStateWithLifecycle()

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    var bottomBarVisible by remember { mutableStateOf(true) }
    val showBottomBar =
        topLevelDestinations.any {
            currentDestination?.hasRoute(it.destination::class) == true
        } &&
            bottomBarVisible &&
            !isOffline

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                FreenowNavigationBar {
                    topLevelDestinations.forEach { destination ->
                        val selected = currentDestination?.hasRoute(destination.destination::class) == true
                        FreenowNavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(destination.destination) {
                                    // Pop back to home to avoid building up a large back stack
                                    // when the user switches between tabs repeatedly
                                    popUpTo<NavDestination.Home> {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(destination.icon),
                                    contentDescription = stringResource(destination.label),
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            selectedIcon = {
                                Icon(
                                    painter = painterResource(destination.selectedIcon),
                                    contentDescription = stringResource(destination.label),
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = {
                                Text(stringResource(destination.label))
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavDestination.Home,
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                bottom = if (showBottomBar) innerPadding.calculateBottomPadding() else 0.dp
            )
        ) {
            composable<NavDestination.Home> { backStackEntry ->
                BookingRoute(
                    isOffline = isOffline,
                    savedStateHandle = backStackEntry.savedStateHandle,
                    onShowBottomBar = { bottomBarVisible = it },
                    onNavigateToDestination = {
                        navController.navigate(NavDestination.Destination)
                    }
                )
            }
            composable<NavDestination.Destination> {
                DestinationRoute(
                    isOffline = isOffline,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateBackWithResult = { pickup, dropoff ->
                        val previousEntry = navController.previousBackStackEntry
                        previousEntry?.savedStateHandle?.apply {
                            set("destination_set", true)
                            set("pickup_text", pickup)
                            set("dropoff_text", dropoff)
                        }
                        navController.popBackStack()
                    }
                )
            }
            composable<NavDestination.Trips> { /* Left blank */ }
            composable<NavDestination.Wallet> { /* Left blank */ }
            composable<NavDestination.Account> { /* Left blank */ }
        }
    }
}
