package dev.guillemdiaz.freenowdemo.feature.booking

import dev.guillemdiaz.freenowdemo.R
import dev.guillemdiaz.freenowdemo.core.model.Vehicle
import dev.guillemdiaz.freenowdemo.feature.booking.state.VehicleUiModel

/**
 * Maps a domain [Vehicle] to a [VehicleUiModel] ready for display in the UI.
 * Mapping is index-based to reflect the order returned by the API.
 */
fun Vehicle.toUiModel(): VehicleUiModel {
    // Dynamically chooses the title based on the data
    val titleRes = when {
        this.maxSeats > 4 -> R.string.taxi_xl
        this.id.endsWith("3") -> R.string.taxi_green
        else -> R.string.taxi_fixed_price
    }

    return VehicleUiModel(
        id = this.id,
        titleRes = titleRes,
        subtitle = "in ${this.etaMinutes} min · ${this.maxSeats} seats",
        price = this.price,
        iconRes = R.drawable.img_taxi
    )
}
