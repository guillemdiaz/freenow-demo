package dev.guillemdiaz.freenowdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import dev.guillemdiaz.freenowdemo.core.designsystem.theme.FreenowTheme
import dev.guillemdiaz.freenowdemo.core.network.NetworkMonitor
import jakarta.inject.Inject

/**
 * Primary entry point for the application, implementing the Single Activity Architecture.
 * Responsible for initializing the Jetpack Compose UI graph and providing top-level
 * injected dependencies (like [NetworkMonitor]) to the [FreenowApp] composable.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FreenowTheme {
                FreenowApp(networkMonitor = networkMonitor)
            }
        }
    }
}
