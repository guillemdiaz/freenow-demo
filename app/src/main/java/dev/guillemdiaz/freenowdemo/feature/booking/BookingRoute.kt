package dev.guillemdiaz.freenowdemo.feature.booking

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.guillemdiaz.freenowdemo.feature.booking.state.BookingStep
import dev.guillemdiaz.freenowdemo.feature.booking.state.BookingViewEffect
import dev.guillemdiaz.freenowdemo.feature.booking.state.BookingViewIntent
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
    val pickupText by savedStateHandle.getStateFlow("pickup_text", "").collectAsStateWithLifecycle()
    val dropoffText by savedStateHandle.getStateFlow("dropoff_text", "").collectAsStateWithLifecycle()

    SideEffect {
        onShowBottomBar(state.currentStep == BookingStep.SEARCH)
    }

    LaunchedEffect(isDestinationSet) {
        if (isDestinationSet) {
            delay(300)
            // Tells the ViewModel to change the UI step
            viewModel.processIntent(BookingViewIntent.DestinationConfirmed(pickupText, dropoffText))
            // Deletes the messages so they don't trigger again
            savedStateHandle.remove<Boolean>("destination_set")
            savedStateHandle.remove<String>("pickup_text")
            savedStateHandle.remove<String>("dropoff_text")
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
