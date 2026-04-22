package dev.guillemdiaz.freenowdemo.core.model

/**
 * Core domain model representing a vehicle available for booking.
 * @param id Unique identifier for the vehicle.
 * @param type The category of the vehicle (e.g., TAXI, RENTAL_CAR).
 * @param latitude The current GPS latitude of the vehicle.
 * @param longitude The current GPS longitude of the vehicle.
 * @param etaMinutes Estimated time of arrival to the user's pickup location in minutes.
 * @param maxSeats The maximum passenger capacity of the vehicle.
 * @param price The calculated fixed price for the requested journey.
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
