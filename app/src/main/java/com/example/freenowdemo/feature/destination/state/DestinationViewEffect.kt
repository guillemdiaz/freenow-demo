package com.example.freenowdemo.feature.destination.state

/**
 * Represents one-shot side effects produced by the Destination ViewModel that the UI must act on
 * exactly once.
 */
sealed class DestinationViewEffect {
    object NavigateBackWithResult : DestinationViewEffect()
}
