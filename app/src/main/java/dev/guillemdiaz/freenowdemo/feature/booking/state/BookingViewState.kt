package dev.guillemdiaz.freenowdemo.feature.booking.state

import dev.guillemdiaz.freenowdemo.core.model.Vehicle

enum class BookingStep {
    SEARCH,
    SELECT_VEHICLE,
    CONFIRM_RIDE
}

data class VehicleUiModel(val id: String, val title: String, val subtitle: String, val price: String, val iconRes: Int)

/**
 * Represents the complete UI state of the Booking screen at any given moment.
 */
data class BookingViewState(
    val isLoading: Boolean = false,
    val isRideBooked: Boolean = false,
    val vehicles: List<Vehicle> = emptyList(),
    val vehicleOptions: List<VehicleUiModel> = emptyList(),
    val selectedVehicle: String? = null,
    val currentStep: BookingStep = BookingStep.SEARCH,
    val pickupLocation: String? = null,
    val dropoffLocation: String? = null
)
