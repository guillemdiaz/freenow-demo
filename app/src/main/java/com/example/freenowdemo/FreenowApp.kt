package com.example.freenowdemo

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.freenowdemo.core.designsystem.component.FreenowNavigationBar
import com.example.freenowdemo.core.designsystem.component.FreenowNavigationBarItem
import com.example.freenowdemo.feature.booking.BookingScreen
import com.example.freenowdemo.feature.destination.DestinationScreen
import com.example.freenowdemo.feature.destination.DestinationViewModel
import com.example.freenowdemo.feature.destination.state.DestinationViewEffect
import com.example.freenowdemo.ui.navigation.NavDestination
import com.example.freenowdemo.ui.navigation.topLevelDestinations

/**
 * Root composable of the app. Sets up the navigation controller, bottom navigation bar,
 * and the NavHost with all the top-level destinations.
 */
@Composable
fun FreenowApp() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            FreenowNavigationBar {
                topLevelDestinations.forEach { destination ->
                    val selected = currentRoute == destination.destination.route
                    FreenowNavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(destination.destination.route) {
                                // Pop back to home to avoid building up a large back stack
                                // when the user switches between tabs repeatedly
                                popUpTo(NavDestination.Home.route) {
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
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavDestination.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // TODO: Route pattern cleanup for all feature screens. Split into XxxRoute.kt (ViewModel, effects, nav)
            //  and XxxScreen.kt (state + onIntent)
            composable(NavDestination.Home.route) {
                BookingScreen(
                    onNavigateToDestination = {
                        navController.navigate(NavDestination.Destination.route)
                    }
                )
            }
            composable(NavDestination.Destination.route) {
                val viewModel = hiltViewModel<DestinationViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()
                LaunchedEffect(Unit) {
                    viewModel.effect.collect { effect ->
                        when (effect) {
                            is DestinationViewEffect.NavigateBackWithResult -> {
                                println("EFFECT: Navigate back")
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("destination_set", true)
                                navController.popBackStack()
                            }
                        }
                    }
                }
                DestinationScreen(
                    state = state,
                    onIntent = { intent -> viewModel.processIntent(intent) },
                    onBackClick = { navController.popBackStack() },
                    onAddStopClick = { /* TODO */ }
                )
            }
            composable(NavDestination.Trips.route) { /* Left blank */ }
            composable(NavDestination.Wallet.route) { /* Left blank */ }
            composable(NavDestination.Account.route) { /* Left blank */ }
        }
    }
}
