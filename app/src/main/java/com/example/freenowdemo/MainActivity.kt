package com.example.freenowdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.freenowdemo.core.designsystem.theme.FreenowTheme
import com.example.freenowdemo.core.network.NetworkMonitor
import dagger.hilt.android.AndroidEntryPoint
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
