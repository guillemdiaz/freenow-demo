package dev.guillemdiaz.freenowdemo.feature.booking.state

/**
 * Represents one-shot side effects produced by the Booking ViewModel that the UI must act on
 * exactly once.
 */
sealed class BookingViewEffect {
    data class NavigateToDestinationSearch(val preselectedService: String? = null) : BookingViewEffect()
    data class NavigateToSetSavedLocation(val locationType: String) : BookingViewEffect()
}
