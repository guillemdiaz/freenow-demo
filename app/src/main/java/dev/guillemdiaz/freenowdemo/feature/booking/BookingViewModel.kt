package dev.guillemdiaz.freenowdemo.feature.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.guillemdiaz.freenowdemo.core.analytics.AnalyticsTracker
import dev.guillemdiaz.freenowdemo.core.data.repository.VehicleRepository
import dev.guillemdiaz.freenowdemo.core.model.Result
import dev.guillemdiaz.freenowdemo.feature.booking.state.BookingStep
import dev.guillemdiaz.freenowdemo.feature.booking.state.BookingViewEffect
import dev.guillemdiaz.freenowdemo.feature.booking.state.BookingViewIntent
import dev.guillemdiaz.freenowdemo.feature.booking.state.BookingViewState
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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
 */
@HiltViewModel
class BookingViewModel @Inject constructor(
    private val repository: VehicleRepository,
    private val analyticsTracker: AnalyticsTracker
) : ViewModel() {
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

            is BookingViewIntent.SearchBarClicked -> {
                viewModelScope.launch {
                    _effect.send(BookingViewEffect.NavigateToDestinationSearch(preselectedService = null))
                }
            }

            is BookingViewIntent.ServiceCardClicked -> {
                viewModelScope.launch {
                    _effect.send(BookingViewEffect.NavigateToDestinationSearch(preselectedService = intent.serviceType))
                }
            }

            is BookingViewIntent.SavedLocationClicked -> {
                viewModelScope.launch {
                    _effect.send(BookingViewEffect.NavigateToSetSavedLocation(locationType = intent.locationType))
                }
            }

            is BookingViewIntent.DestinationConfirmed -> {
                _state.update {
                    it.copy(
                        currentStep = BookingStep.SELECT_VEHICLE,
                        pickupLocation = intent.pickup,
                        dropoffLocation = intent.dropoff
                    )
                }
            }

            is BookingViewIntent.BackToSearchClicked -> {
                _state.update { it.copy(currentStep = BookingStep.SEARCH, selectedVehicle = null) }
            }

            is BookingViewIntent.ConfirmRideClicked -> {
                _state.update { it.copy(currentStep = BookingStep.CONFIRM_RIDE) }
            }

            is BookingViewIntent.BackToVehicleSelectionClicked -> {
                _state.update { it.copy(currentStep = BookingStep.SELECT_VEHICLE) }
            }

            is BookingViewIntent.OrderRideClicked -> orderRide()

            is BookingViewIntent.DismissSuccessDialog -> resetBookingState()
        }
    }

    /**
     * Fetches the list of available vehicles from the repository.
     */
    private fun loadVehicles() {
        viewModelScope.launch {
            repository.getVehicles().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }

                    is Result.Success -> {
                        val uiOptions = result.data.mapIndexed { index, vehicle ->
                            vehicle.toUiModel(index)
                        }
                        _state.update {
                            it.copy(
                                isLoading = false,
                                vehicles = result.data,
                                vehicleOptions = uiOptions
                            )
                        }
                    }

                    is Result.Error -> {
                        _state.update { it.copy(isLoading = false) }
                    }
                }
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
     * Triggers the final checkout process.
     */
    private fun orderRide() {
        val currentState = _state.value
        val vehicleId = currentState.selectedVehicle ?: return
        val vehicle = currentState.vehicleOptions.find { it.id == vehicleId }

        analyticsTracker.trackEvent(
            eventName = "Ride_Booked",
            parameters = mapOf(
                "vehicle_type" to (vehicle?.title ?: "Unknown"),
                "pickup" to (currentState.pickupLocation ?: "Current Location"),
                "dropoff" to (currentState.dropoffLocation ?: "Destination"),
                "price" to (vehicle?.price ?: "Unknown")
            )
        )

        _state.update { it.copy(isLoading = true) }

        // Simulates a network request
        viewModelScope.launch {
            delay(2500)

            // On success, resets the flow back to the beginning!
            _state.update {
                it.copy(
                    isLoading = false,
                    isRideBooked = true
                )
            }
        }
    }

    private fun resetBookingState() {
        _state.update {
            it.copy(
                isRideBooked = false,
                currentStep = BookingStep.SEARCH,
                selectedVehicle = null,
                pickupLocation = null,
                dropoffLocation = null
            )
        }
    }
}
