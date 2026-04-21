package dev.guillemdiaz.freenowdemo.feature.booking.state

/**
 * UI representation of a vehicle option shown in the booking sheet.
 * Derived from the domain [Vehicle] model via [toUiModel].
 */
data class VehicleUiModel(val id: String, val title: String, val subtitle: String, val price: String, val iconRes: Int)
