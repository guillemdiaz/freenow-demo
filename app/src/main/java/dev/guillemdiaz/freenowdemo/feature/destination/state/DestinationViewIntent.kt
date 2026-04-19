package dev.guillemdiaz.freenowdemo.feature.destination.state

/**
 * Represents all possible user actions and system events that the Destination feature can handle.
 */
sealed interface DestinationViewIntent {
    data class UpdatePickup(val text: String) : DestinationViewIntent
    data class UpdateDropoff(val text: String) : DestinationViewIntent
    data object ConfirmClicked : DestinationViewIntent
}
