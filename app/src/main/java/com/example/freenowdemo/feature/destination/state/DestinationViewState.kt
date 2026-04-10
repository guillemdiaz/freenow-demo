package com.example.freenowdemo.feature.destination.state

/**
 * Represents the complete UI state of the Destination screen at any given moment.
 */
data class DestinationViewState(
    val pickupText: String = "",
    val dropoffText: String = "",
    // Only enabled if both fields have some text
    val isConfirmEnabled: Boolean = false
)
