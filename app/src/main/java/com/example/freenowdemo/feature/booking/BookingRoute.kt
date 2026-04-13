package com.example.freenowdemo.feature.booking

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.freenowdemo.feature.booking.state.BookingViewEffect
import com.example.freenowdemo.feature.booking.state.BookingViewIntent

/**
 * Smart wrapper for [BookingScreen] that owns the ViewModel, collects state,
 * handles one-off navigation effects, and receives the destination handshake via
 * SavedStateHandle
 */
@Suppress("ParamsComparedByRef")
@Composable
fun BookingRoute(
    savedStateHandle: SavedStateHandle,
    onNavigateToDestination: () -> Unit,
    viewModel: BookingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isDestinationSet by savedStateHandle.getStateFlow("destination_set", false).collectAsStateWithLifecycle()

    LaunchedEffect(isDestinationSet) {
        if (isDestinationSet) {
            // Tells the ViewModel to change the UI step
            viewModel.processIntent(BookingViewIntent.DestinationConfirmed)
            // Deletes the message so it doesn't trigger again
            savedStateHandle.remove<Boolean>("destination_set")
        }
    }

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
