package dev.guillemdiaz.freenowdemo.feature.booking.state

/**
 * Represents all possible user actions and system events that the Booking feature can handle.
 */
sealed interface BookingViewIntent {
    data object LoadVehicles : BookingViewIntent
    data class SelectVehicle(val vehicleId: String) : BookingViewIntent
    object SearchBarClicked : BookingViewIntent
    data class ServiceCardClicked(val serviceType: String) : BookingViewIntent
    data class SavedLocationClicked(val locationType: String) : BookingViewIntent

    /** Triggered automatically when returning from the Destination screen with results */
    data class DestinationConfirmed(val pickup: String, val dropoff: String) : BookingViewIntent

    data object BackToSearchClicked : BookingViewIntent
    data object ConfirmRideClicked : BookingViewIntent
    data object BackToVehicleSelectionClicked : BookingViewIntent
    data object OrderRideClicked : BookingViewIntent
    data object DismissSuccessDialog : BookingViewIntent
}
