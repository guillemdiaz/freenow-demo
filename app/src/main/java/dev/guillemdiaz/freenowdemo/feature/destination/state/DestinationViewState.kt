package dev.guillemdiaz.freenowdemo.feature.destination.state

/**
 * Represents the complete UI state of the Destination screen at any given moment.
 * @param pickupText The current text in the pickup location field.
 * @param dropoffText The current text in the dropoff location field.
 * @param isConfirmEnabled Determines if the confirm button is clickable. Only true if both fields contain text.
 */
data class DestinationViewState(
    val pickupText: String = "",
    val dropoffText: String = "",
    val isConfirmEnabled: Boolean = false
)
