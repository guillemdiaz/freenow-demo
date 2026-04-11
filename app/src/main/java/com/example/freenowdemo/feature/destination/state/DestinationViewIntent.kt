package com.example.freenowdemo.feature.destination.state

/**
 * Represents all possible user actions and system events that the Destination feature can handle.
 */
sealed class DestinationViewIntent {
    data class UpdatePickup(val text: String) : DestinationViewIntent()
    data class UpdateDropoff(val text: String) : DestinationViewIntent()
    object ConfirmClicked : DestinationViewIntent()
}
