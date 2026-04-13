package com.example.freenowdemo.feature.booking.state

import com.example.freenowdemo.core.model.Vehicle

/**
 * Represents the complete UI state of the Booking screen at any given moment.
 */
data class BookingViewState(
    val isLoading: Boolean = false,
    val vehicles: List<Vehicle> = emptyList(),
    val selectedVehicle: String? = null,
    val driverEnRoute: Boolean = false
)
