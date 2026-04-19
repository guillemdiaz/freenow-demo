package dev.guillemdiaz.freenowdemo.feature.booking

import dev.guillemdiaz.freenowdemo.R
import dev.guillemdiaz.freenowdemo.core.model.Vehicle
import dev.guillemdiaz.freenowdemo.feature.booking.state.VehicleUiModel

/**
 * Maps a domain [Vehicle] to a [VehicleUiModel] ready for display in the UI.
 * Mapping is index-based to reflect the order returned by the API.
 */
fun Vehicle.toUiModel(index: Int): VehicleUiModel = when (index) {
    0 -> VehicleUiModel(
        id = id,
        title = "Taxi Fixed Price",
        subtitle = "in 1 min · 4 seats",
        price = "16.60 €",
        iconRes = R.drawable.img_taxi
    )

    1 -> VehicleUiModel(
        id = id,
        title = "Taxi XL Fixed Price",
        subtitle = "in 3 min · 5-8 seats",
        price = "21.20 €",
        iconRes = R.drawable.img_taxi
    )

    else -> VehicleUiModel(
        id = id,
        title = "Taxi Green",
        subtitle = "in 1 min · 4 seats",
        price = "16.60 €",
        iconRes = R.drawable.img_taxi
    )
}
