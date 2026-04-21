package dev.guillemdiaz.freenowdemo.feature.booking.state

/**
 * Represents the sequential steps of the booking flow.
 * The UI transitions through these steps as the user progresses.
 */
enum class BookingStep {
    SEARCH,
    SELECT_VEHICLE,
    CONFIRM_RIDE
}
