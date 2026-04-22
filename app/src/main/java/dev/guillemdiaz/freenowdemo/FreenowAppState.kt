package dev.guillemdiaz.freenowdemo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dev.guillemdiaz.freenowdemo.core.network.NetworkMonitor
import dev.guillemdiaz.freenowdemo.ui.navigation.NavDestination as AppNavDestination
import dev.guillemdiaz.freenowdemo.ui.navigation.TopLevelDestination
import dev.guillemdiaz.freenowdemo.ui.navigation.topLevelDestinations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Holds app-wide UI state that needs to be shared across the entire app.
 * @param navController The app's single [NavHostController].
 * @param coroutineScope The scope used to convert flows into [StateFlow]s.
 * @param networkMonitor The monitor used to observe network connectivity changes.
 */
@Stable
class FreenowAppState(
    val navController: NavHostController,
    val topLevelDestinations: List<TopLevelDestination>,
    coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor
) {
    /**
     * Derived from [NetworkMonitor.isOnline] - true when the device has no active network.
     */
    val isOffline: StateFlow<Boolean> = networkMonitor.isOnline
        .map { !it }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    /**
     * The current [NavDestination], or null if the back stack is empty.
     */
    val currentDestination: NavDestination?
        get() = navController.currentBackStackEntry?.destination

    /**
     * Whether the bottom navigation bar should be visible.
     * True only when the current destination is a top-level destination.
     */
    fun shouldShowBottomBar(
        currentDestination: NavDestination?,
        isOffline: Boolean,
        bottomBarVisible: Boolean
    ): Boolean = topLevelDestinations.any {
        currentDestination?.hasRoute(it.destination::class) == true
    } &&
        bottomBarVisible &&
        !isOffline

    /**
     * Navigates to a [TopLevelDestination], popping up to Home to avoid
     * building up a large back stack.
     */
    fun navigateToTopLevelDestination(destination: TopLevelDestination) {
        navController.navigate(destination.destination) {
            popUpTo<AppNavDestination.Home> {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    /**
     * Returns true if the given [TopLevelDestination] is currently selected.
     */
    fun isDestinationSelected(currentDestination: NavDestination?, destination: TopLevelDestination): Boolean =
        currentDestination?.hasRoute(destination.destination::class) == true
}

/**
 * Creates and remembers a [FreenowAppState] instance across recompositions.
 */
@Composable
fun rememberFreenowAppState(
    networkMonitor: NetworkMonitor,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): FreenowAppState = remember(networkMonitor, navController, coroutineScope) {
    FreenowAppState(navController, topLevelDestinations, coroutineScope, networkMonitor)
}
