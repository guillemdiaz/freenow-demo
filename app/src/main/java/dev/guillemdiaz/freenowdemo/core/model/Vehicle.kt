package dev.guillemdiaz.freenowdemo.core.model

/**
 * Core domain model representing a vehicle available for booking.
 */
data class Vehicle(
    val id: String,
    val type: VehicleType,
    val latitude: Double,
    val longitude: Double,
    val etaMinutes: Int,
    val maxSeats: Int,
    val price: Double
)
