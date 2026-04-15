package com.example.freenowdemo.feature.booking

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.freenowdemo.feature.booking.state.BookingStep
import com.example.freenowdemo.feature.booking.state.BookingViewEffect
import com.example.freenowdemo.feature.booking.state.BookingViewIntent
import kotlinx.coroutines.delay

/**
 * Smart wrapper for [BookingScreen] that owns the ViewModel, collects state,
 * handles one-off navigation effects, and receives the destination handshake via
 * SavedStateHandle
 */
@Suppress("ParamsComparedByRef")
@Composable
fun BookingRoute(
    isOffline: Boolean,
    savedStateHandle: SavedStateHandle,
    onShowBottomBar: (Boolean) -> Unit,
    onNavigateToDestination: () -> Unit,
    viewModel: BookingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isDestinationSet by savedStateHandle.getStateFlow("destination_set", false).collectAsStateWithLifecycle()

    LaunchedEffect(state.currentStep) {
        onShowBottomBar(state.currentStep != BookingStep.SELECT_VEHICLE)
    }

    LaunchedEffect(isDestinationSet) {
        if (isDestinationSet) {
            delay(300)
            // Tells the ViewModel to change the UI step
            viewModel.processIntent(BookingViewIntent.DestinationConfirmed)
            // Deletes the message so it doesn't trigger again
            savedStateHandle.remove<Boolean>("destination_set")
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is BookingViewEffect.NavigateToDestinationSearch -> {
                    onNavigateToDestination() // navigate first
                    onShowBottomBar(false) // hide after
                }

                is BookingViewEffect.NavigateToSetSavedLocation -> { /* TODO */ }
            }
        }
    }

    BookingScreen(
        isOffline = isOffline,
        state = state,
        onIntent = viewModel::processIntent
    )
}
