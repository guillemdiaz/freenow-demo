package com.example.freenowdemo.feature.booking.state

/**
 * Represents one-shot side effects produced by the Booking ViewModel that the UI must act on
 * exactly once.
 */
sealed class BookingViewEffect {
    object ShowNoConnectionBanner : BookingViewEffect()
    object NavigateToSuccess : BookingViewEffect()
}
