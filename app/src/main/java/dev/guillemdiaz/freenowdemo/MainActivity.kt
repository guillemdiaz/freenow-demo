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
 * Single activity that hosts the entire app.
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
