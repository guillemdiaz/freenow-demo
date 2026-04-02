package com.example.freenowdemo

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.freenowdemo.core.designsystem.component.FreenowNavigationBar
import com.example.freenowdemo.core.designsystem.component.FreenowNavigationBarItem
import com.example.freenowdemo.feature.booking.BookingScreen
import com.example.freenowdemo.ui.navigation.NavDestination
import com.example.freenowdemo.ui.navigation.topLevelDestinations

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
                                popUpTo(NavDestination.Home.route) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
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
            composable(NavDestination.Home.route) { BookingScreen() }
            // TODO: the depeer screens
            /*
            composable("taxi_detail/{taxiId}") { backStackEntry ->
                val taxiId = backStackEntry.arguments?.getString("taxiId")
                TaxiDetailScreen(
                    taxiId = taxiId,
                    onBackClick = { navController.popBackStack() }
                )
            }
             */
            composable(NavDestination.Trips.route) { /* (leave blank) */ }
            composable(NavDestination.Wallet.route) { /* (leave blank) */ }
            composable(NavDestination.Account.route) { /* (leave blank) */ }
        }
    }
}
