package com.example.freenowdemo.feature.booking

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.freenowdemo.feature.booking.state.BookingViewEffect

@Composable
fun BookingRoute(onNavigateToDestination: () -> Unit, viewModel: BookingViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is BookingViewEffect.NavigateToDestinationSearch -> onNavigateToDestination()
                is BookingViewEffect.NavigateToSetSavedLocation -> { /* TODO */ }
                is BookingViewEffect.ShowNoConnectionBanner -> { /* TODO */ }
            }
        }
    }

    BookingScreen(
        state = state,
        onIntent = viewModel::processIntent
    )
}
