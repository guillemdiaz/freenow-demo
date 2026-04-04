package com.example.freenowdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.freenowdemo.core.designsystem.theme.FreenowTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single activity that hosts the entire app.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FreenowTheme {
                FreenowApp()
            }
        }
    }
}
