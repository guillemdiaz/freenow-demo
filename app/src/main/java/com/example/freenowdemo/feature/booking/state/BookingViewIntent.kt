package com.example.freenowdemo.feature.booking.state

/**
 * Represents all possible user actions and system events that the Booking feature can handle.
 */
sealed class BookingViewIntent {
    object LoadVehicles : BookingViewIntent()
    data class SelectVehicle(val vehicleId: String) : BookingViewIntent()
    object BookRideClicked : BookingViewIntent()
    object DismissOfflineBanner : BookingViewIntent()
}
