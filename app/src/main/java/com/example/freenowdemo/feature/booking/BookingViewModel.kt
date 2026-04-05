package com.example.freenowdemo.feature.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freenowdemo.core.data.repository.VehicleRepository
import com.example.freenowdemo.feature.booking.state.BookingViewEffect
import com.example.freenowdemo.feature.booking.state.BookingViewIntent
import com.example.freenowdemo.feature.booking.state.BookingViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the Booking feature, implemented following the MVI (Model-View-Intent) pattern.
 * Responsibilities:
 * - Holds and exposes the [BookingViewState] as an observable [StateFlow].
 * - Processes user [BookingViewIntent]s and translates them into state mutations or side effects.
 * - Emits one-shot [BookingViewEffect]s via a [Channel].
 * Injected via Hilt, the repository dependency is put out until the real implementation is wired in.
 */
@HiltViewModel
class BookingViewModel @Inject constructor(private val repository: VehicleRepository) : ViewModel() {
    private val _state = MutableStateFlow(BookingViewState())
    val state = _state.asStateFlow()

    // Channel guarantees UI events are never dropped during configuration changes
    private val _effect = Channel<BookingViewEffect>()
    val effect = _effect.receiveAsFlow()

    // As soon as the ViewModel is created, grabs the vehicles
    init {
        processIntent(BookingViewIntent.LoadVehicles)
    }

    /**
     * Single entry point for all user interactions and system events.
     * Dispatches each [BookingViewIntent] to the appropriate private handler.
     * @param intent The action triggered by the user or the system.
     */
    fun processIntent(intent: BookingViewIntent) {
        when (intent) {
            is BookingViewIntent.LoadVehicles -> loadVehicles()
            is BookingViewIntent.SelectVehicle -> selectVehicle(intent.vehicleId)
            is BookingViewIntent.BookRideClicked -> bookRide()
            is BookingViewIntent.DismissOfflineBanner -> dismissOfflineBanner()
        }
    }

    /**
     * Fetches the list of available vehicles from the repository.
     */
    private fun loadVehicles() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val vehicles = repository.getVehicles()

                _state.update {
                    it.copy(
                        isLoading = false,
                        isOffline = false,
                        vehicles = vehicles
                    )
                }
            } catch (e: Exception) {
                // Network or data-layer failure: surface offline state and notify the UI
                _state.update { it.copy(isLoading = false, isOffline = true) }
                _effect.send(BookingViewEffect.ShowNoConnectionBanner)
            }
        }
    }

    /**
     * Updates the currently selected vehicle in the state.
     */
    private fun selectVehicle(vehicleId: String) {
        _state.update { it.copy(selectedVehicle = vehicleId) }
    }

    /**
     * Handles the "Book Ride" button tap.
     * Emits [BookingViewEffect.NavigateToSuccess] to instruct the UI to navigate to the
     * success screen.
     * TODO: trigger the Lottie animation state before navigating.
     */
    private fun bookRide() {
        viewModelScope.launch {
            _effect.send(BookingViewEffect.NavigateToSuccess)
        }
    }

    /**
     * Clears the offline banner by setting [BookingViewState.isOffline] to false.
     * Called when the user explicitly dismisses the no-connection banner.
     */
    private fun dismissOfflineBanner() {
        _state.update { it.copy(isOffline = false) }
    }
}
