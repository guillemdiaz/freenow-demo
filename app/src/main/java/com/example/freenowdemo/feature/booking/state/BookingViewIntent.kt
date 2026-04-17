package com.example.freenowdemo.feature.booking.state

/**
 * Represents all possible user actions and system events that the Booking feature can handle.
 */
sealed class BookingViewIntent {
    object LoadVehicles : BookingViewIntent()
    data class SelectVehicle(val vehicleId: String) : BookingViewIntent()
    object SearchBarClicked : BookingViewIntent()
    data class ServiceCardClicked(val serviceType: String) : BookingViewIntent()
    data class SavedLocationClicked(val locationType: String) : BookingViewIntent()
    data class DestinationConfirmed(val pickup: String, val dropoff: String) : BookingViewIntent()
    object BackToSearchClicked : BookingViewIntent()
    object ConfirmRideClicked : BookingViewIntent()
    object BackToVehicleSelectionClicked : BookingViewIntent()
    object OrderRideClicked : BookingViewIntent()
}
