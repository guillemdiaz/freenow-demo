package dev.guillemdiaz.freenowdemo.feature.booking.state

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * UI representation of a vehicle option shown in the booking sheet.
 * Derived from the domain [Vehicle] model via [toUiModel].
 */
data class VehicleUiModel(
    val id: String,
    @param:StringRes val titleRes: Int,
    val subtitle: String,
    val price: Double,
    @param:DrawableRes val iconRes: Int
)
